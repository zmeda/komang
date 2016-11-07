package org.zalando.komang.model

import org.zalando.komang.model.Model.Application

object Commands {
  case class CreateApplication(application: Application)

  case class CreateApplicationResponse(application: Application)
}
