package org.zalando.komang.query

import akka.Done
import org.zalando.komang.model.Model.{Application, ApplicationId}
import org.zalando.komang.persistence.Tables

import scala.concurrent.Future

trait KomangDAO {
  def getAllApplications(): Future[Seq[Tables.ApplicationRow]]

  def getApplication(applicationId: ApplicationId): Future[Option[Tables.ApplicationRow]]

  def createApplication(application: Application): Future[Done]
}
