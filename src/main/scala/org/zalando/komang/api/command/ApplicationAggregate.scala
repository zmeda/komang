package org.zalando.komang.api.command

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import org.zalando.komang.model.{Commands, Events}
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
        sender() ! Commands.CreateApplicationResponse(app)
      }
  }

  def updateState(event: Events.ApplicationCreated): Unit =
    applicationState = event.application
}
