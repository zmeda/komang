package org.zalando.komang.model

import org.zalando.komang.model.Model.ApplicationId

object Events {
  sealed trait Event

  case class ApplicationCreated(applicationId: ApplicationId, name: String) extends Event
}
