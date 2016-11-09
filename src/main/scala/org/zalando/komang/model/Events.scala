package org.zalando.komang.model

import org.zalando.komang.model.Model.Application

object Events {
  sealed trait Event

  case class ApplicationCreated(application: Application) extends Event
}
