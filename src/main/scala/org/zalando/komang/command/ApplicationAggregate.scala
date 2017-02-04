package org.zalando.komang.command

import akka.actor.{ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import akka.persistence.PersistentActor
import org.zalando.komang.model.command._
import org.zalando.komang.model.event._
import org.zalando.komang.model.response._
import org.zalando.komang.model.Model.{Application, Config, Profile}

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
    case ccc: CreateConfigCommand =>
      persist(ConfigCreatedEvent(ccc.profileId, ccc.configId, ccc.name, ccc.`type`, ccc.value)) { evt =>
        updateState(evt)
        sender() ! CreateConfigResponse(evt.configId)
      }
    case ucc: UpdateConfigCommand =>
      val events = List(
        ucc.name.map(n => ConfigNameUpdatedEvent(ucc.profileId, ucc.configId, n)),
        ucc.`type`.map(t => ConfigTypeUpdatedEvent(ucc.profileId, ucc.configId, t)),
        ucc.value.map(v => ConfigValueUpdatedEvent(ucc.profileId, ucc.configId, v))
      ).collect {
        case Some(evt) => evt
      }
      persistAll(events) { evt =>
        updateState(evt)
        evt match {
          case ConfigValueUpdatedEvent(profileId, configId, _) =>
            val config = applicationState.profiles.find(_.profileId == profileId).get.configs.find(_.configId == configId).get
            sender() ! UpdateConfigResponse(config)
        }
      }
  }

  def updateState(event: Event): Unit =
    event match {
      case ApplicationCreatedEvent(applicationId, name) =>
        applicationState = Application(applicationId, name)
      case ApplicationUpdatedEvent(applicationId, name) =>
        applicationState = Application(applicationId, name)
      case ProfileCreatedEvent(_, profileId, name) =>
        applicationState = applicationState.copy(profiles = applicationState.profiles :+ Profile(profileId, name))
      case ProfileUpdatedEvent(_, profileId, name) =>
        applicationState = applicationState.copy(profiles = applicationState.profiles.map {
          case Profile(`profileId`, _, configs) => Profile(profileId, name, configs)
          case profile => profile
        })
      case ConfigCreatedEvent(profileId, configId, name, cType, value) =>
        applicationState = applicationState.copy(profiles = applicationState.profiles.map {
          case Profile(`profileId`, pName, configs) => Profile(profileId, pName, configs :+ Config(configId, name, cType, value))
          case profile => profile
        })
      case ConfigNameUpdatedEvent(profileId, configId, name) =>
        applicationState = applicationState.copy(profiles = applicationState.profiles.map {
          case Profile(`profileId`, pName, configs) => Profile(profileId, pName, configs :+ Config(configId, name, cType, value))
          case profile => profile
        })
        // TODO add other events impl
    }
}

object ApplicationAggregate {
  def props = Props(classOf[ApplicationAggregate])

//  val extractEntityId: ShardRegion.ExtractEntityId = {
//    case c: Command => (c.applicationId.value.toString, c)
//  }
//
//  private val numberOfShards = 100
//
//  val extractShardId: ShardRegion.ExtractShardId = {
//    case c: Command => Math.abs(c.applicationId.value.hashCode() % numberOfShards).toString
//  }
}
