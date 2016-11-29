package org.zalando.komang.service

import org.zalando.komang.model.Model.{Application, ApplicationDraft, ApplicationId}

import scala.concurrent.Future

trait KomangService {
  def listApplications: Future[Vector[Application]]

  def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId]
}
