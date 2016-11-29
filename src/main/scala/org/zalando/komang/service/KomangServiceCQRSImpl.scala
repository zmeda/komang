package org.zalando.komang.service

import java.util.UUID

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.zalando.komang.command.ApplicationAggregate
import org.zalando.komang.model.{Commands, Responses}
import org.zalando.komang.model.Model.{Application, ApplicationDraft, ApplicationId}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class KomangServiceCQRSImpl(implicit val actorSystem: ActorSystem) extends KomangService {
  implicit val timeout: Timeout = Timeout(500.millis)

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  override def listApplications: Future[Vector[Application]] = {
    ???
  }

  override def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId] = {
    val applicationIdUUID = UUID.randomUUID
    val persistentActor = actorSystem.actorOf(ApplicationAggregate.props, applicationIdUUID.toString)
    persistentActor ? Commands.CreateApplication(ApplicationId(applicationIdUUID), applicationDraft.name) map {
      case response: Responses.CreateApplicationResponse => response.applicationId
    }
  }
}
