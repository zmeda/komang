package org.zalando.komang.model

import java.util.UUID

object Model {
  case class ApplicationId(value: UUID)

  case class Application(applicationId: ApplicationId, name: String)
}
