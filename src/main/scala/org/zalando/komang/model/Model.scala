package org.zalando.komang.model

import java.util.UUID

object Model {
  case class ApplicationId(value: UUID)

  case class ApplicationName(value: String)

  case class Application(applicationId: ApplicationId, name: ApplicationName, profiles: List[Profile] = List.empty)

  case class ProfileId(value: UUID)

  case class ProfileName(value: String)

  case class Profile(profileId: ProfileId, name: ProfileName)
}
