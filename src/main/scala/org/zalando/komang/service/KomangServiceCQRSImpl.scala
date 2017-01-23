package org.zalando.komang.service

import java.util.UUID

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.zalando.komang.api.ApiModel.{ApplicationDraft, ApplicationUpdate}
import org.zalando.komang.command.ApplicationAggregate
import org.zalando.komang.model.Model.{Application, ApplicationId}
import org.zalando.komang.model.command._
import org.zalando.komang.model.response._
import org.zalando.komang.query.KomangDAO

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class KomangServiceCQRSImpl(komangDAO: KomangDAO)(implicit val actorSystem: ActorSystem) extends KomangService {
  implicit val timeout: Timeout = Timeout(500.millis)

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  override def listApplications: Future[Vector[Application]] = {
    komangDAO
      .getAllApplications()
      .map(_.map {
        case applicationRow =>
          Application(applicationRow.applicationId, applicationRow.name)
      }.toVector)
  }

  override def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId] = {
    val applicationIdUUID = UUID.randomUUID
    val persistentActor = actorSystem.actorOf(ApplicationAggregate.props, applicationIdUUID.toString)
    persistentActor ? CreateApplicationCommand(ApplicationId(applicationIdUUID), applicationDraft.name) map {
      case response: CreateApplicationResponse => response.applicationId
    }
  }

  override def findApplication(applicationId: ApplicationId): Future[Option[Application]] = {
    komangDAO
      .getApplication(applicationId)
      .map(_.map {
        case applicationRow =>
          Application(applicationRow.applicationId, applicationRow.name)
      })
  }

  override def updateApplication(applicationUpdate: ApplicationUpdate): Future[Application] = {
    ???
    //val persistentActor = actorSystem.actorOf(ApplicationAggregate.props, applicationId.value.toString)
    //persistentActor ? UpdateApplicationCommand(applicationId, )
  }
}
