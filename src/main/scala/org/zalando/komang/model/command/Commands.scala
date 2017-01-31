package org.zalando.komang.model.command

import org.zalando.komang.model.Model.{ApplicationId, ApplicationName, ProfileId, ProfileName}

sealed trait Command {
  def applicationId: ApplicationId
}

final case class CreateApplicationCommand(applicationId: ApplicationId, name: ApplicationName) extends Command

final case class UpdateApplicationCommand(applicationId: ApplicationId, name: ApplicationName) extends Command

final case class CreateProfileCommand(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName)
    extends Command

final case class UpdateProfileCommand(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName)
    extends Command
