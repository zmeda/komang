package org.zalando.komang.service

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import org.zalando.komang.api.ApiModel._
import org.zalando.komang.model.Model._
import org.zalando.komang.model.command._
import org.zalando.komang.model.response._
import org.zalando.komang.query.KomangDAO

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class KomangServiceCQRSImpl(komangDAO: KomangDAO, shardingRegion: ActorRef)(implicit val ec: ExecutionContext,
                                                                            implicit val actorSystem: ActorSystem)
    extends KomangService {
  implicit val timeout: Timeout = Timeout(500.millis)

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
    shardingRegion ? CreateApplicationCommand(ApplicationId(applicationIdUUID),
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
    shardingRegion ? UpdateApplicationCommand(applicationId, applicationUpdate.name) map {
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
    shardingRegion ? CreateProfileCommand(applicationId,
                                                                   ProfileId(UUID.randomUUID),
                                                                   profileDraft.name) map {
      case response: CreateProfileResponse => (response.applicationId, response.profileId)
    }
  }

  override def updateProfile(applicationId: ApplicationId,
                             profileId: ProfileId,
                             profileUpdate: ProfileUpdate): Future[Profile] = {
    shardingRegion ? UpdateProfileCommand(applicationId, profileId, profileUpdate.name) map {
      case response: UpdateProfileResponse => response.profile
    }
  }

  override def listConfigs(profileId: ProfileId): Future[Vector[Config]] = {
    komangDAO
      .getAllConfigs(profileId)
      .map(_.map {
        case configRow =>
          Config(configRow.configId,
                 ConfigName(configRow.name),
                 ConfigType(configRow.`type`),
                 ConfigValue(configRow.value))
      }.toVector)
  }

  override def findConfig(profileId: ProfileId, configId: ConfigId): Future[Option[Config]] = {
    komangDAO
      .getConfig(profileId, configId)
      .map(_.map {
        case configRow =>
          Config(configRow.configId,
                 ConfigName(configRow.name),
                 ConfigType(configRow.`type`),
                 ConfigValue(configRow.value))
      })
  }

  override def createConfig(applicationId: ApplicationId,
                            profileId: ProfileId,
                            configDraft: ConfigDraft): Future[ConfigId] = {
    shardingRegion ? CreateConfigCommand(applicationId,
                                                                  profileId,
                                                                  ConfigId(UUID.randomUUID),
                                                                  configDraft.name,
                                                                  configDraft.`type`,
                                                                  configDraft.value) map {
      case response: CreateConfigResponse => response.configId
    }
  }

  override def updateConfig(applicationId: ApplicationId,
                            profileId: ProfileId,
                            configUpdate: ConfigUpdate): Future[Config] = {
    ???
  }
}
