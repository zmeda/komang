package org.zalando.komang.model.response

import org.zalando.komang.model.Model.ApplicationId

sealed trait Response

case class CreateApplicationResponse(applicationId: ApplicationId) extends Response
