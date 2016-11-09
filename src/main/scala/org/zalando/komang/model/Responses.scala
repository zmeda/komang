package org.zalando.komang.model

import org.zalando.komang.model.Model.Application

object Responses {
  sealed trait Response

  case class CreateApplicationResponse(application: Application) extends Response
}
