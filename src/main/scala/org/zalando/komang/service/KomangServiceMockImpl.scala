package org.zalando.komang.service
import java.util.UUID

import org.zalando.komang.model.Model.{Application, ApplicationDraft, ApplicationId}

import scala.concurrent.{ExecutionContext, Future}

class KomangServiceMockImpl(implicit ec: ExecutionContext) extends KomangService {
  private val applicationList = Vector(Application(ApplicationId(UUID.randomUUID()), "Test Name"))

  override def listApplications: Future[Vector[Application]] =
    Future(applicationList)

  override def createApplication(applicationDraft: ApplicationDraft): Future[ApplicationId] =
    Future(ApplicationId(UUID.randomUUID()))
}
