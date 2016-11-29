package org.zalando.komang.command

import akka.actor.{ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import akka.persistence.PersistentActor
import org.zalando.komang.model.Commands.Command
import org.zalando.komang.model.{Commands, Events, Responses}
import org.zalando.komang.model.Model.Application

class ApplicationAggregate extends PersistentActor with ActorLogging {
  val name = context.self.path.name

  override def persistenceId: String = name

  var applicationState: Application = _

  override def receiveRecover: Receive = {
    case evt: Events.Event =>
      updateState(evt)
  }

  override def receiveCommand: Receive = {
    case cmd: Commands.CreateApplication =>
      persist(Events.ApplicationCreated(cmd.applicationId, cmd.name)) { evt =>
        updateState(evt)
        sender() ! Responses.CreateApplicationResponse(evt.applicationId)
      }
  }

  def updateState(event: Events.Event): Unit =
    event match {
      case Events.ApplicationCreated(applicationId, name) =>
        applicationState = Application(applicationId, name)
    }
}

object ApplicationAggregate {
  def props = Props(classOf[ApplicationAggregate])

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case c: Command => (c.applicationId.value.toString, c)
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case c: Command => Math.abs(c.applicationId.value.hashCode() % numberOfShards).toString
  }
}
