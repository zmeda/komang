package org.zalando.komang.model.event

import org.zalando.komang.model.Model.ApplicationId
import org.zalando.komang.serialization.KomangProtobufSerializable

sealed trait Event extends KomangProtobufSerializable

case class ApplicationCreatedEvent(applicationId: ApplicationId, name: String) extends Event

case class ApplicationUpdatedEvent(applicationId: ApplicationId, name: String) extends Event
