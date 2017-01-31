package org.zalando.komang.model.response

import org.zalando.komang.model.Model._

sealed trait Response

case class CreateApplicationResponse(applicationId: ApplicationId) extends Response

case class UpdateApplicationResponse(application: Application) extends Response

case class CreateProfileResponse(applicationId: ApplicationId, profileId: ProfileId) extends Response

case class UpdateProfileResponse(profile: Profile) extends Response
