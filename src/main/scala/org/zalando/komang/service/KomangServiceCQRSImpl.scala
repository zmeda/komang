package org.zalando.komang.service

import java.util.UUID

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.zalando.komang.api.ApiModel._
import org.zalando.komang.command.ApplicationAggregate
import org.zalando.komang.model.Model._
import org.zalando.komang.model.command._
import org.zalando.komang.model.response._
import org.zalando.komang.query.KomangDAO

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class KomangServiceCQRSImpl(komangDAO: KomangDAO)(implicit val actorSystem: ActorSystem) extends KomangService {
  implicit val timeout: Timeout = Timeout(500.millis)

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  private def getPersistentActor(applicationIdUUID: UUID) =
    actorSystem.actorOf(ApplicationAggregate.props, applicationIdUUID.toString)

  override def listApplications: Future[Vector[Application]] = {
    komangDAO
      .getAllApplications()
      .map(_.map {
        case applicationRow =>
          Application(applicationRow.applicationId, ApplicationName(applicationRow.name))
      }.toVector)
  }

  override def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId] = {
    val applicationIdUUID = UUID.randomUUID
    getPersistentActor(applicationIdUUID) ? CreateApplicationCommand(ApplicationId(applicationIdUUID),
                                                                     applicationDraft.name) map {
      case response: CreateApplicationResponse => response.applicationId
    }
  }

  override def findApplication(applicationId: ApplicationId): Future[Option[Application]] = {
    komangDAO
      .getApplication(applicationId)
      .map(_.map {
        case applicationRow =>
          Application(applicationRow.applicationId, ApplicationName(applicationRow.name))
      })
  }

  override def updateApplication(applicationId: ApplicationId,
                                 applicationUpdate: ApplicationUpdate): Future[Application] = {
    getPersistentActor(applicationId.value) ? UpdateApplicationCommand(applicationId, applicationUpdate.name) map {
      case response: UpdateApplicationResponse => response.application
    }
  }

  override def listProfiles(applicationId: ApplicationId): Future[Vector[Profile]] = {
    komangDAO
      .getAllProfiles(applicationId)
      .map(_.map {
        case profileRow => Profile(profileRow.profileId, ProfileName(profileRow.name))
      }.toVector)
  }

  override def findProfile(applicationId: ApplicationId, profileId: ProfileId): Future[Option[Profile]] = {
    komangDAO
      .getProfile(applicationId, profileId)
      .map(_.map {
        case profileRow =>
          Profile(profileRow.profileId, ProfileName(profileRow.name))
      })
  }

  override def createProfile(applicationId: ApplicationId,
                             profileDraft: ProfileDraft): Future[(ApplicationId, ProfileId)] = {
    val profileIdUUID = UUID.randomUUID
    getPersistentActor(applicationId.value) ? CreateProfileCommand(applicationId,
                                                                   ProfileId(profileIdUUID),
                                                                   profileDraft.name) map {
      case response: CreateProfileResponse => (response.applicationId, response.profileId)
    }
  }

  override def updateProfile(applicationId: ApplicationId,
                             profileId: ProfileId,
                             profileUpdate: ProfileUpdate): Future[Profile] = {
    getPersistentActor(applicationId.value) ? UpdateProfileCommand(applicationId, profileId, profileUpdate.name) map {
      case response: UpdateProfileResponse => response.profile
    }
  }
}
