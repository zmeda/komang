package org.zalando.komang.api

import org.zalando.komang.model.Model.ApplicationId

object ApiModel {
  case class ApplicationUpdate(name: String)

  case class ApplicationDraft(name: String)

  case class ApplicationNotFoundException(applicationId: ApplicationId) extends Exception
}
