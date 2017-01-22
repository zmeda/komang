package org.zalando.komang.query

import akka.Done
import org.zalando.komang.model.Model.Application
import slick.driver.H2Driver.api._
import org.zalando.komang.persistence.Tables

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class KomangDAOImpl extends KomangDAO {
  val db = Database.forConfig("h2mem")

  override def getAllApplications(): Future[Seq[Tables.ApplicationRow]] = {
    val sql = for {
      a <- Tables.Application
    } yield (a)
    db.run(sql.result)
  }

  override def createApplication(application: Application): Future[Done] = {
    val sql = Tables.Application += Tables.ApplicationRow(application.applicationId.toString, application.name)
    db.run(sql).map(_ => Done)
  }
}
