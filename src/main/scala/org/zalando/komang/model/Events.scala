package org.zalando.komang.model

import org.zalando.komang.model.Model.Application

object Events {
  case class ApplicationCreated(application: Application)
}
