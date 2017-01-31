package org.zalando.komang.api

import org.zalando.komang.model.Model.{ApplicationId, ApplicationName, ProfileId, ProfileName}

object ApiModel {
  case class ApplicationDraft(name: ApplicationName)

  case class ApplicationUpdate(name: ApplicationName)

  case class ProfileDraft(name: ProfileName)

  case class ProfileUpdate(name: ProfileName)

  case class ApplicationNotFoundException(applicationId: ApplicationId) extends Exception

  case class ProfileNotFoundException(applicationId: ApplicationId, profileId: ProfileId) extends Exception
}
