package org.zalando.komang.model.command

import org.zalando.komang.model.Model.ApplicationId

sealed trait Command {
  def applicationId: ApplicationId
}

final case class CreateApplicationCommand(applicationId: ApplicationId, name: String) extends Command

final case class UpdateApplicationCommand(applicationId: ApplicationId, name: String) extends Command
