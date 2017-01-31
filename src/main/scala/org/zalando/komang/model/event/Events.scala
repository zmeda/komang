package org.zalando.komang.model.event

import org.zalando.komang.model.Model.{ApplicationId, ApplicationName, ProfileId, ProfileName}
import org.zalando.komang.serialization.KomangProtobufSerializable

sealed trait Event extends KomangProtobufSerializable

case class ApplicationCreatedEvent(applicationId: ApplicationId, name: ApplicationName) extends Event

case class ApplicationUpdatedEvent(applicationId: ApplicationId, name: ApplicationName) extends Event

case class ProfileCreatedEvent(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName) extends Event

case class ProfileUpdatedEvent(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName) extends Event
