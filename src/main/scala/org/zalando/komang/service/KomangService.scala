package org.zalando.komang.service

import org.zalando.komang.api.ApiModel.{ApplicationDraft, ApplicationUpdate}
import org.zalando.komang.model.Model.{Application, ApplicationId}

import scala.concurrent.Future

trait KomangService {
  def listApplications: Future[Vector[Application]]

  def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId]

  def updateApplication(applicationUpdate: ApplicationUpdate): Future[Application]
}
