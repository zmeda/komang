package org.zalando.komang.model

import org.zalando.komang.model.Model.ApplicationId

object Responses {
  sealed trait Response

  case class CreateApplicationResponse(applicationId: ApplicationId) extends Response
}
