package org.zalando.komang.service

import org.zalando.komang.api.ApiModel.{ApplicationDraft, ApplicationUpdate, ProfileDraft}
import org.zalando.komang.model.Model.{Application, ApplicationId, ProfileId}

import scala.concurrent.Future

trait KomangService {
  def listApplications: Future[Vector[Application]]

  def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId]

  def findApplication(applicationId: ApplicationId): Future[Option[Application]]

  def updateApplication(applicationId: ApplicationId, applicationUpdate: ApplicationUpdate): Future[Application]

  def createProfile(applicationId: ApplicationId, profileDraft: ProfileDraft): Future[(ApplicationId, ProfileId)]
}
