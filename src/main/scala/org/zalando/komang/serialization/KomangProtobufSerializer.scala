package org.zalando.komang
package serialization

import java.util.UUID

import akka.serialization.SerializerWithStringManifest
import org.zalando.komang.model.Model.ApplicationId
import org.zalando.komang.model.event.ApplicationCreatedEvent

class KomangProtobufSerializer extends SerializerWithStringManifest {
  override def identifier: Int = 1234

  override def manifest(o: AnyRef): String = o match {
    case _: ApplicationCreatedEvent => ApplicationCreatedEventManifest
    case _ =>
      throw new IllegalArgumentException(
        s"can't serialize object of type ${o.getClass.getName} in ${getClass.getName}"
      )
  }

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case ace: ApplicationCreatedEvent =>
      protobuf.Events.ApplicationCreatedEvent(ace.applicationId.value.toString, ace.name).toByteArray
    case _ =>
      throw new IllegalArgumentException(
        s"can't serialize object of type ${o.getClass.getName} in ${getClass.getName}"
      )
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case ApplicationCreatedEventManifest =>
      val evt = protobuf.Events.ApplicationCreatedEvent.parseFrom(bytes)
      ApplicationCreatedEvent(ApplicationId(UUID.fromString(evt.applicationId)), evt.name)
    case _ =>
      throw new IllegalArgumentException(
        s"""can't deserialize message with manifest "${manifest}" in ${getClass.getName}"""
      )
  }

  private final val ApplicationCreatedEventManifest = "a"
}
