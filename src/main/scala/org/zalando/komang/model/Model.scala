package org.zalando.komang.model

import java.util.UUID

object Model {
  case class ApplicationId(value: UUID)

  case class ApplicationName(value: String)

  case class Application(applicationId: ApplicationId, name: ApplicationName, profiles: Profiles = List.empty)

  type Profiles = Seq[Profile]

  case class ProfileId(value: UUID)

  case class ProfileName(value: String)

  case class Profile(profileId: ProfileId, name: ProfileName, configs: Configs = List.empty)

  type Configs = Seq[Config]

  case class ConfigId(value: UUID)

  case class ConfigName(value: String)

  case class ConfigType(value: String)

  case class ConfigValue(value: String)

  case class Config(configId: ConfigId, name: ConfigName, `type`: ConfigType, value: ConfigValue)
}
