package org.zalando.komang.api

import org.zalando.komang.model.Model._

object ApiModel {
  case class ApplicationDraft(name: ApplicationName)

  case class ApplicationUpdate(name: ApplicationName)

  case class ProfileDraft(name: ProfileName)

  case class ProfileUpdate(name: ProfileName)

  case class ConfigDraft(name: ConfigName, `type`: ConfigType, value: ConfigValue)

  case class ConfigUpdate(name: Option[ConfigName], `type`: Option[ConfigType], value: Option[ConfigValue])

  case class ApplicationNotFoundException(applicationId: ApplicationId) extends Exception

  case class ProfileNotFoundException(applicationId: ApplicationId, profileId: ProfileId) extends Exception

  case class ConfigNotFoundException(applicationId: ApplicationId, profileId: ProfileId, configId: ConfigId) extends Exception
}
