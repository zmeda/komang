package org.zalando.komang.service

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.zalando.komang.command.ApplicationAggregate
import org.zalando.komang.model.{Commands, Responses}
import org.zalando.komang.model.Model.Application

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class KomangServiceCQRSImpl(implicit val actorSystem: ActorSystem) extends KomangService {
  implicit val timeout: Timeout = Timeout(500.millis)

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  override def listApplications: Future[Vector[Application]] = {
    ???
  }

  override def createApplication(application: Application): Future[Application] = {
    val persistentActor = actorSystem.actorOf(ApplicationAggregate.props)
    persistentActor ? Commands.CreateApplication(application) map {
      case response: Responses.CreateApplicationResponse => response.application
    }
  }
}
