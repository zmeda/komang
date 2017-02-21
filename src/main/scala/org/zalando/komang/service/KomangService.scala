package org.zalando.komang.service

import org.zalando.komang.api.ApiModel._
import org.zalando.komang.model.Model._

import scala.concurrent.Future

trait KomangService {
  def listApplications: Future[Vector[Application]]

  def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId]

  def findApplication(applicationId: ApplicationId): Future[Option[Application]]

  def updateApplication(applicationId: ApplicationId, applicationUpdate: ApplicationUpdate): Future[Application]

  def listProfiles(applicationId: ApplicationId): Future[Vector[Profile]]

  def findProfile(applicationId: ApplicationId, profileId: ProfileId): Future[Option[Profile]]

  def createProfile(applicationId: ApplicationId, profileDraft: ProfileDraft): Future[(ApplicationId, ProfileId)]

  def updateProfile(applicationId: ApplicationId, profileId: ProfileId, profileUpdate: ProfileUpdate): Future[Profile]

  def listConfigs(profileId: ProfileId): Future[Vector[Config]]

  def findConfig(profileId: ProfileId, configId: ConfigId): Future[Option[Config]]

  def createConfig(applicationId: ApplicationId, profileId: ProfileId, configDraft: ConfigDraft): Future[ConfigId]

  def updateConfig(applicationId: ApplicationId,
                   profileId: ProfileId,
                   configId: ConfigId,
                   configUpdate: ConfigUpdate): Future[Config]
}
