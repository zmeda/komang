package org.zalando.komang.query

import org.zalando.komang.model.Model.Application

import scala.concurrent.Future

trait KomangDAO {
  def getAllApplications(): Future[Vector[Application]]
}
