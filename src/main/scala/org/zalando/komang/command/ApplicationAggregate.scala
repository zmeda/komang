package org.zalando.komang.command

import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor
import org.zalando.komang.model.{Commands, Events, Responses}
import org.zalando.komang.model.Model.Application

class ApplicationAggregate extends PersistentActor with ActorLogging {
  val name = context.self.path.name

  override def persistenceId: String = name

  var applicationState: Application = _

  override def receiveRecover: Receive = {
    case evt: Events.ApplicationCreated =>
      updateState(evt)
  }

  override def receiveCommand: Receive = {
    case _ @ Commands.CreateApplication(app) =>
      persist(Events.ApplicationCreated(app)) { evt =>
        updateState(evt)
        sender() ! Responses.CreateApplicationResponse(app)
      }
  }

  def updateState(event: Events.ApplicationCreated): Unit =
    applicationState = event.application
}

object ApplicationAggregate {
  def props = Props(classOf[ApplicationAggregate])
}
