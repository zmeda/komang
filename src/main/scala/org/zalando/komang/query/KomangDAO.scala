package org.zalando.komang.query

import akka.Done
import org.zalando.komang.model.Model._
import org.zalando.komang.persistence.Tables

import scala.concurrent.Future

trait KomangDAO {
  def getAllApplications(): Future[Seq[Tables.ApplicationRow]]

  def getApplication(applicationId: ApplicationId): Future[Option[Tables.ApplicationRow]]

  def createApplication(application: Application): Future[Done]

  def updateApplication(application: Application): Future[Done]

  def getAllProfiles(applicationId: ApplicationId): Future[Seq[Tables.ProfileRow]]

  def getProfile(applicationId: ApplicationId, profileId: ProfileId): Future[Option[Tables.ProfileRow]]

  def createProfile(applicationId: ApplicationId, profile: Profile): Future[Done]

  def updateProfile(applicationId: ApplicationId, profile: Profile): Future[Done]

  def getAllConfigs(profileId: ProfileId): Future[Seq[Tables.ConfigRow]]

  def getConfig(profileId: ProfileId, configId: ConfigId): Future[Option[Tables.ConfigRow]]

  def createConfig(profileId: ProfileId, config: Config): Future[Done]

  def updateConfig(profileId: ProfileId, config: Config): Future[Done]
}
