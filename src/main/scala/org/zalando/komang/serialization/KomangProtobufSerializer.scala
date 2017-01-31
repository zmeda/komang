package org.zalando.komang
package serialization

import java.util.UUID

import akka.serialization.SerializerWithStringManifest
import org.zalando.komang.model.Model.{ApplicationId, ApplicationName, ProfileId, ProfileName}
import org.zalando.komang.model.event._

class KomangProtobufSerializer extends SerializerWithStringManifest {
  override def identifier: Int = 1234

  override def manifest(o: AnyRef): String = o match {
    case _: ApplicationCreatedEvent => ApplicationCreatedEventManifest
    case _: ApplicationUpdatedEvent => ApplicationUpdatedEventManifest
    case _: ProfileCreatedEvent => ProfileCreatedEventManifest
    case _: ProfileUpdatedEvent => ProfileUpdatedEventManifest
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
    case _ =>
      throw new IllegalArgumentException(
        s"""can't deserialize message with manifest "${manifest}" in ${getClass.getName}"""
      )
  }

  private final val ApplicationCreatedEventManifest = "a"
  private final val ApplicationUpdatedEventManifest = "b"
  private final val ProfileCreatedEventManifest = "c"
  private final val ProfileUpdatedEventManifest = "d"
}
