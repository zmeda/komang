package org.zalando.komang.model

import org.zalando.komang.model.Model.ApplicationId

object Commands {
  sealed trait Command {
    def applicationId: ApplicationId
  }

  final case class CreateApplication(applicationId: ApplicationId, name: String) extends Command
}
