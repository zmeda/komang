package org.zalando.komang.model.command

import org.zalando.komang.model.Model._

sealed trait Command {
  def applicationId: ApplicationId
}

final case class CreateApplicationCommand(applicationId: ApplicationId, name: ApplicationName) extends Command

final case class UpdateApplicationCommand(applicationId: ApplicationId, name: ApplicationName) extends Command

final case class CreateProfileCommand(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName)
    extends Command

final case class UpdateProfileCommand(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName)
    extends Command

final case class CreateConfigCommand(applicationId: ApplicationId,
                                     profileId: ProfileId,
                                     configId: ConfigId,
                                     name: ConfigName,
                                     `type`: ConfigType,
                                     value: ConfigValue)
    extends Command

final case class UpdateConfigCommand(applicationId: ApplicationId,
                                     profileId: ProfileId,
                                     configId: ConfigId,
                                     name: Option[ConfigName],
                                     `type`: Option[ConfigType],
                                     value: Option[ConfigValue])
    extends Command
