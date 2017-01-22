package org.zalando.komang.command

import akka.actor.{ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import akka.persistence.PersistentActor
import org.zalando.komang.model.command._
import org.zalando.komang.model.event._
import org.zalando.komang.model.response._
import org.zalando.komang.model.Model.Application

class ApplicationAggregate extends PersistentActor with ActorLogging {
  val name = context.self.path.name

  override def persistenceId: String = name

  var applicationState: Application = _

  override def receiveRecover: Receive = {
    case evt: Event =>
      updateState(evt)
  }

  override def receiveCommand: Receive = {
    case cmd: CreateApplicationCommand =>
      persist(ApplicationCreatedEvent(cmd.applicationId, cmd.name)) { evt =>
        updateState(evt)
        sender() ! CreateApplicationResponse(evt.applicationId)
      }
  }

  def updateState(event: Event): Unit =
    event match {
      case ApplicationCreatedEvent(applicationId, name) =>
        applicationState = Application(applicationId, name)
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
