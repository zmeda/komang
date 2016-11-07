package org.zalando.komang.service
import java.util.UUID

import org.zalando.komang.model.Model.{Application, ApplicationId}

import scala.concurrent.{ExecutionContext, Future}

class KomangServiceMockImpl(implicit ec: ExecutionContext) extends KomangService {
  private val applicationList = Vector(Application(Some(ApplicationId(UUID.randomUUID())), "Test Name"))

  override def listApplications: Future[Vector[Application]] =
    Future(applicationList)

  override def createApplication(application: Application): Future[Application] =
    Future(application)
}
