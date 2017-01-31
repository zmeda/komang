package org.zalando.komang.command

import akka.actor.{ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import akka.persistence.PersistentActor
import org.zalando.komang.model.command._
import org.zalando.komang.model.event._
import org.zalando.komang.model.response._
import org.zalando.komang.model.Model.{Application, Profile}

class ApplicationAggregate extends PersistentActor with ActorLogging {
  val name = context.self.path.name

  override def persistenceId: String = name

  var applicationState: Application = _

  override def receiveRecover: Receive = {
    case evt: Event =>
      updateState(evt)
  }

  override def receiveCommand: Receive = {
    case cac: CreateApplicationCommand =>
      persist(ApplicationCreatedEvent(cac.applicationId, cac.name)) { evt =>
        updateState(evt)
        sender() ! CreateApplicationResponse(evt.applicationId)
      }
    case uac: UpdateApplicationCommand =>
      persist(ApplicationUpdatedEvent(uac.applicationId, uac.name)) { evt =>
        updateState(evt)
        sender() ! UpdateApplicationResponse(Application(evt.applicationId, evt.name))
      }
    case cpc: CreateProfileCommand =>
      persist(ProfileCreatedEvent(cpc.applicationId, cpc.profileId, cpc.name)) { evt =>
        updateState(evt)
        sender() ! CreateProfileResponse(evt.applicationId, evt.profileId)
      }
    case upc: UpdateProfileCommand =>
      persist(ProfileCreatedEvent(upc.applicationId, upc.profileId, upc.name)) { evt =>
        updateState(evt)
        sender() ! UpdateProfileResponse(Profile(evt.profileId, evt.name))
      }
  }

  def updateState(event: Event): Unit =
    event match {
      case ApplicationCreatedEvent(applicationId, name) =>
        applicationState = Application(applicationId, name)
      case ApplicationUpdatedEvent(applicationId, name) =>
        applicationState = Application(applicationId, name)
      case ProfileCreatedEvent(_, profileId, name) =>
        applicationState = applicationState.copy(profiles = Profile(profileId, name) :: applicationState.profiles)
      case ProfileUpdatedEvent(_, profileId, name) =>
        applicationState = applicationState.copy(profiles = applicationState.profiles map {
          case Profile(`profileId`, _) => Profile(profileId, name)
          case profile => profile
        })
    }
}

object ApplicationAggregate {
  def props = Props(classOf[ApplicationAggregate])

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case c: Command => (c.applicationId.value.toString, c)
  }

  private val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case c: Command => Math.abs(c.applicationId.value.hashCode() % numberOfShards).toString
  }
}
