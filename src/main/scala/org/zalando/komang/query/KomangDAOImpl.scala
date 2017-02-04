package org.zalando.komang.query

import akka.Done
import org.zalando.komang.model.Model.{Application, Config, Profile, ApplicationId, ProfileId, ConfigId, ConfigName, ConfigType, ConfigValue}
import slick.driver.H2Driver.api._
import org.zalando.komang.persistence.Tables
import org.zalando.komang.persistence.Tables._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class KomangDAOImpl extends KomangDAO {

  val db = Database.forConfig("h2mem")

  override def getAllApplications(): Future[Seq[Tables.ApplicationRow]] = {
    db.run(Tables.Application.result)
  }

  override def getApplication(applicationId: ApplicationId): Future[Option[Tables.ApplicationRow]] = {
    db.run(Tables.Application.filter(_.applicationId === applicationId).result.headOption)
  }

  override def createApplication(application: Application): Future[Done] = {
    db.run(Tables.Application += Tables.ApplicationRow(application.applicationId, application.name.value))
      .map(_ => Done)
  }

  override def updateApplication(application: Application): Future[Done] = {
    val findApp = Tables.Application.filter(_.applicationId === application.applicationId)
    db.run(findApp.map(_.name).update(application.name.value)).map(_ => Done)
  }

  override def getAllProfiles(applicationId: ApplicationId): Future[Seq[Tables.ProfileRow]] = {
    db.run(Tables.Profile.filter(_.applicationId === applicationId).result)
  }

  override def getProfile(applicationId: ApplicationId, profileId: ProfileId): Future[Option[Tables.ProfileRow]] = {
    db.run(
      Tables.Profile.filter(p => p.applicationId === applicationId && p.profileId === profileId).result.headOption)
  }

  override def createProfile(applicationId: ApplicationId, profile: Profile): Future[Done] = {
    db.run(Tables.Profile += Tables.ProfileRow(profile.profileId, applicationId, profile.name.value)).map(_ => Done)
  }

  override def updateProfile(applicationId: ApplicationId, profile: Profile): Future[Done] = {
    val findProfile =
      Tables.Profile.filter(p => p.applicationId === applicationId && p.profileId === profile.profileId)
    db.run(findProfile.map(_.name).update(profile.name.value)).map(_ => Done)
  }

  override def getAllConfigs(profileId: ProfileId): Future[Seq[Tables.ConfigRow]] = {
    db.run(Tables.Config.filter(_.profileId === profileId).result)
  }

  override def getConfig(profileId: ProfileId, configId: ConfigId): Future[Option[Tables.ConfigRow]] = {
    db.run(Tables.Config.filter(c => c.profileId === profileId && c.configId === configId).result.headOption)
  }

  override def createConfig(profileId: ProfileId, config: Config): Future[Done] = {
    db.run(Tables.Config += Tables.ConfigRow(config.configId, profileId, config.name.value, config.`type`.value, config.value.value)) map (_ => Done)
  }

  override def updateConfigName(profileId: ProfileId, configId: ConfigId, name: ConfigName): Future[Done] = {
    val findConfig = Tables.Config.filter(c => c.profileId === profileId && c.configId === configId)
    db.run(findConfig.map(_.name).update(name.value)).map(_ => Done)
  }

  override def updateConfigType(profileId: ProfileId, configId: ConfigId, `type`: ConfigType): Future[Done] = {
    val findConfig = Tables.Config.filter(c => c.profileId === profileId && c.configId === configId)
    db.run(findConfig.map(_.`type`).update(`type`.value)).map(_ => Done)
  }

  override def updateConfigValue(profileId: ProfileId, configId: ConfigId, value: ConfigValue): Future[Done] = {
    val findConfig = Tables.Config.filter(c => c.profileId === profileId && c.configId === configId)
    db.run(findConfig.map(_.value).update(value.value)).map(_ => Done)
  }
}
