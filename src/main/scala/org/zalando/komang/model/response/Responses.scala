package org.zalando.komang.model.response

import org.zalando.komang.model.Model.{Application, ApplicationId}

sealed trait Response

case class CreateApplicationResponse(applicationId: ApplicationId) extends Response

case class UpdateApplicationResponse(application: Application) extends Response
