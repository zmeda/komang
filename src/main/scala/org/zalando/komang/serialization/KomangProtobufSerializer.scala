package org.zalando.komang
package serialization

import java.util.UUID

import akka.serialization.SerializerWithStringManifest
import org.zalando.komang.model.Model._
import org.zalando.komang.model.event._

class KomangProtobufSerializer extends SerializerWithStringManifest {
  override def identifier: Int = 1234

  override def manifest(o: AnyRef): String = o match {
    case _: ApplicationCreatedEvent => ApplicationCreatedEventManifest
    case _: ApplicationUpdatedEvent => ApplicationUpdatedEventManifest
    case _: ProfileCreatedEvent => ProfileCreatedEventManifest
    case _: ProfileUpdatedEvent => ProfileUpdatedEventManifest
    case _: ConfigCreatedEvent => ConfigCreatedEventManifest
    case _: ConfigNameUpdatedEvent => ConfigNameUpdatedEventManifest
    case _: ConfigTypeUpdatedEvent => ConfigTypeUpdatedEventManifest
    case _: ConfigValueUpdatedEvent => ConfigValueUpdatedEventManifest
    case _ =>
      throw new IllegalArgumentException(
        s"can't serialize object of type ${o.getClass.getName} in ${getClass.getName}"
      )
  }

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case ace: ApplicationCreatedEvent =>
      protobuf.Events.ApplicationCreatedEvent(ace.applicationId.value.toString, ace.name.value).toByteArray
    case aue: ApplicationUpdatedEvent =>
      protobuf.Events.ApplicationUpdatedEvent(aue.applicationId.value.toString, aue.name.value).toByteArray
    case pce: ProfileCreatedEvent =>
      protobuf.Events
        .ProfileCreatedEvent(pce.applicationId.value.toString, pce.profileId.value.toString, pce.name.value)
        .toByteArray
    case pue: ProfileUpdatedEvent =>
      protobuf.Events
        .ProfileUpdatedEvent(pue.applicationId.value.toString, pue.profileId.value.toString, pue.name.value)
        .toByteArray
    case cce: ConfigCreatedEvent =>
      protobuf.Events
        .ConfigCreatedEvent(cce.profileId.value.toString,
                            cce.configId.value.toString,
                            cce.name.value,
                            cce.`type`.value,
                            cce.value.value)
        .toByteArray
    case cnue: ConfigNameUpdatedEvent =>
      protobuf.Events
        .ConfigNameUpdatedEvent(cnue.profileId.value.toString, cnue.configId.value.toString, cnue.name.value)
        .toByteArray
    case ctue: ConfigTypeUpdatedEvent =>
      protobuf.Events
        .ConfigTypeUpdatedEvent(ctue.profileId.value.toString, ctue.configId.value.toString, ctue.`type`.value)
        .toByteArray
    case cvue: ConfigValueUpdatedEvent =>
      protobuf.Events
        .ConfigValueUpdatedEvent(cvue.profileId.value.toString, cvue.configId.value.toString, cvue.value.value)
        .toByteArray
    case _ =>
      throw new IllegalArgumentException(
        s"can't serialize object of type ${o.getClass.getName} in ${getClass.getName}"
      )
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case ApplicationCreatedEventManifest =>
      val evt = protobuf.Events.ApplicationCreatedEvent.parseFrom(bytes)
      ApplicationCreatedEvent(ApplicationId(UUID.fromString(evt.applicationId)), ApplicationName(evt.name))
    case ApplicationUpdatedEventManifest =>
      val evt = protobuf.Events.ApplicationUpdatedEvent.parseFrom(bytes)
      ApplicationUpdatedEvent(ApplicationId(UUID.fromString(evt.applicationId)), ApplicationName(evt.name))
    case ProfileCreatedEventManifest =>
      val evt = protobuf.Events.ProfileCreatedEvent.parseFrom(bytes)
      ProfileCreatedEvent(ApplicationId(UUID.fromString(evt.applicationId)),
                          ProfileId(UUID.fromString(evt.profileId)),
                          ProfileName(evt.name))
    case ProfileUpdatedEventManifest =>
      val evt = protobuf.Events.ProfileUpdatedEvent.parseFrom(bytes)
      ProfileUpdatedEvent(ApplicationId(UUID.fromString(evt.applicationId)),
                          ProfileId(UUID.fromString(evt.profileId)),
                          ProfileName(evt.name))
    case ConfigCreatedEventManifest =>
      val evt = protobuf.Events.ConfigCreatedEvent.parseFrom(bytes)
      ConfigCreatedEvent(ProfileId(UUID.fromString(evt.profileId)),
                         ConfigId(UUID.fromString(evt.configId)),
                         ConfigName(evt.name),
                         ConfigType(evt.`type`),
                         ConfigValue(evt.value))
    case ConfigNameUpdatedEventManifest =>
      val evt = protobuf.Events.ConfigNameUpdatedEvent.parseFrom(bytes)
      ConfigNameUpdatedEvent(ProfileId(UUID.fromString(evt.profileId)),
                             ConfigId(UUID.fromString(evt.configId)),
                             ConfigName(evt.name))
    case ConfigTypeUpdatedEventManifest =>
      val evt = protobuf.Events.ConfigTypeUpdatedEvent.parseFrom(bytes)
      ConfigTypeUpdatedEvent(ProfileId(UUID.fromString(evt.profileId)),
                             ConfigId(UUID.fromString(evt.configId)),
                             ConfigType(evt.`type`))
    case ConfigValueUpdatedEventManifest =>
      val evt = protobuf.Events.ConfigValueUpdatedEvent.parseFrom(bytes)
      ConfigValueUpdatedEvent(ProfileId(UUID.fromString(evt.profileId)),
                              ConfigId(UUID.fromString(evt.configId)),
                              ConfigValue(evt.value))
    case _ =>
      throw new IllegalArgumentException(
        s"""can't deserialize message with manifest "${manifest}" in ${getClass.getName}"""
      )
  }

  private final val ApplicationCreatedEventManifest = "a"
  private final val ApplicationUpdatedEventManifest = "b"
  private final val ProfileCreatedEventManifest = "c"
  private final val ProfileUpdatedEventManifest = "d"
  private final val ConfigCreatedEventManifest = "e"
  private final val ConfigNameUpdatedEventManifest = "f"
  private final val ConfigTypeUpdatedEventManifest = "g"
  private final val ConfigValueUpdatedEventManifest = "h"
}
