package org.zalando.komang.service

import org.zalando.komang.model.Model.Application

import scala.concurrent.Future

trait KomangService {
  def listApplications: Future[Vector[Application]]

  def createApplication(application: Application): Future[Application]
}
