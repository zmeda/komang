package org.zalando.komang.service

import akka.actor.ActorSystem
import akka.pattern.ask
import org.zalando.komang.command.ApplicationAggregate
import org.zalando.komang.model.{Commands, Responses}
import org.zalando.komang.model.Model.Application

import scala.concurrent.Future

class KomangServiceCQRSImpl(implicit val actorSystem: ActorSystem) extends KomangService {
  override def listApplications: Future[Vector[Application]] = ???

  override def createApplication(application: Application): Future[Application] = {
    val persistentActor = actorSystem.actorOf(ApplicationAggregate.props)
    persistentActor ? Commands.CreateApplication(application) map {
      case response: Responses.CreateApplicationResponse => response.application
    }
  }
}
