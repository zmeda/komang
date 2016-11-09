package org.zalando.komang.model

import org.zalando.komang.model.Model.Application

object Commands {
  sealed trait Command

  case class CreateApplication(application: Application) extends Command
}
