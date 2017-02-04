package org.zalando.komang.model.event

import org.zalando.komang.model.Model._
import org.zalando.komang.serialization.KomangProtobufSerializable

sealed trait Event extends KomangProtobufSerializable

case class ApplicationCreatedEvent(applicationId: ApplicationId, name: ApplicationName) extends Event

case class ApplicationUpdatedEvent(applicationId: ApplicationId, name: ApplicationName) extends Event

case class ProfileCreatedEvent(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName) extends Event

case class ProfileUpdatedEvent(applicationId: ApplicationId, profileId: ProfileId, name: ProfileName) extends Event

case class ConfigCreatedEvent(profileId: ProfileId,
                              configId: ConfigId,
                              name: ConfigName,
                              `type`: ConfigType,
                              value: ConfigValue)
    extends Event

case class ConfigNameUpdatedEvent(profileId: ProfileId, configId: ConfigId, name: ConfigName) extends Event

case class ConfigTypeUpdatedEvent(profileId: ProfileId, configId: ConfigId, `type`: ConfigType) extends Event

case class ConfigValueUpdatedEvent(profileId: ProfileId, configId: ConfigId, value: ConfigValue) extends Event
