package org.zalando.komang.query

import akka.Done
import org.zalando.komang.model.Model.{Application, ApplicationId, Profile, ProfileId}
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
    db.run(Tables.Profile += Tables.ProfileRow(profile.profileId, applicationId, profile.name.value)) map (_ => Done)
  }

  override def updateProfile(applicationId: ApplicationId, profile: Profile): Future[Done] = {
    val findProfile =
      Tables.Profile.filter(p => p.applicationId === applicationId && p.profileId === profile.profileId)
    db.run(findProfile.map(_.name).update(profile.name.value)).map(_ => Done)
  }
}
