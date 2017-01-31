// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package org.zalando.komang.protobuf.Events

@SerialVersionUID(0L)
final case class ProfileCreatedEvent(
    applicationId: String = "",
    profileId: String = "",
    name: String = ""
) extends com.trueaccord.scalapb.GeneratedMessage
    with com.trueaccord.scalapb.Message[ProfileCreatedEvent]
    with com.trueaccord.lenses.Updatable[ProfileCreatedEvent] {
  @transient
  private[this] var __serializedSizeCachedValue: Int = 0
  private[this] def __computeSerializedValue(): Int = {
    var __size = 0
    if (applicationId != "") {
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, applicationId)
    }
    if (profileId != "") { __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, profileId) }
    if (name != "") { __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, name) }
    __size
  }
  final override def serializedSize: Int = {
    var read = __serializedSizeCachedValue
    if (read == 0) {
      read = __computeSerializedValue()
      __serializedSizeCachedValue = read
    }
    read
  }
  def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): Unit = {
    {
      val __v = applicationId
      if (__v != "") {
        _output__.writeString(1, __v)
      }
    };
    {
      val __v = profileId
      if (__v != "") {
        _output__.writeString(2, __v)
      }
    };
    {
      val __v = name
      if (__v != "") {
        _output__.writeString(3, __v)
      }
    };
  }
  def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream)
    : org.zalando.komang.protobuf.Events.ProfileCreatedEvent = {
    var __applicationId = this.applicationId
    var __profileId = this.profileId
    var __name = this.name
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 10 =>
          __applicationId = _input__.readString()
        case 18 =>
          __profileId = _input__.readString()
        case 26 =>
          __name = _input__.readString()
        case tag => _input__.skipField(tag)
      }
    }
    org.zalando.komang.protobuf.Events.ProfileCreatedEvent(
      applicationId = __applicationId,
      profileId = __profileId,
      name = __name
    )
  }
  def withApplicationId(__v: String): ProfileCreatedEvent = copy(applicationId = __v)
  def withProfileId(__v: String): ProfileCreatedEvent = copy(profileId = __v)
  def withName(__v: String): ProfileCreatedEvent = copy(name = __v)
  def getField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor): scala.Any = {
    __field.getNumber match {
      case 1 => {
        val __t = applicationId
        if (__t != "") __t else null
      }
      case 2 => {
        val __t = profileId
        if (__t != "") __t else null
      }
      case 3 => {
        val __t = name
        if (__t != "") __t else null
      }
    }
  }
  override def toString: String = _root_.com.trueaccord.scalapb.TextFormat.printToUnicodeString(this)
  def companion = org.zalando.komang.protobuf.Events.ProfileCreatedEvent
}

object ProfileCreatedEvent
    extends com.trueaccord.scalapb.GeneratedMessageCompanion[org.zalando.komang.protobuf.Events.ProfileCreatedEvent] {
  implicit def messageCompanion: com.trueaccord.scalapb.GeneratedMessageCompanion[
    org.zalando.komang.protobuf.Events.ProfileCreatedEvent] = this
  def fromFieldsMap(
      __fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, scala.Any])
    : org.zalando.komang.protobuf.Events.ProfileCreatedEvent = {
    require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor),
            "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    org.zalando.komang.protobuf.Events.ProfileCreatedEvent(
      __fieldsMap.getOrElse(__fields.get(0), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(1), "").asInstanceOf[String],
      __fieldsMap.getOrElse(__fields.get(2), "").asInstanceOf[String]
    )
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor =
    EventsProto.javaDescriptor.getMessageTypes.get(2)
  def messageCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor)
    : _root_.com.trueaccord.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__field)
  def enumCompanionForField(__field: _root_.com.google.protobuf.Descriptors.FieldDescriptor)
    : _root_.com.trueaccord.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__field)
  lazy val defaultInstance = org.zalando.komang.protobuf.Events.ProfileCreatedEvent(
    )
  implicit class ProfileCreatedEventLens[UpperPB](
      _l: _root_.com.trueaccord.lenses.Lens[UpperPB, org.zalando.komang.protobuf.Events.ProfileCreatedEvent])
      extends _root_.com.trueaccord.lenses.ObjectLens[UpperPB, org.zalando.komang.protobuf.Events.ProfileCreatedEvent](
        _l) {
    def applicationId: _root_.com.trueaccord.lenses.Lens[UpperPB, String] =
      field(_.applicationId)((c_, f_) => c_.copy(applicationId = f_))
    def profileId: _root_.com.trueaccord.lenses.Lens[UpperPB, String] =
      field(_.profileId)((c_, f_) => c_.copy(profileId = f_))
    def name: _root_.com.trueaccord.lenses.Lens[UpperPB, String] = field(_.name)((c_, f_) => c_.copy(name = f_))
  }
  final val APPLICATION_ID_FIELD_NUMBER = 1
  final val PROFILE_ID_FIELD_NUMBER = 2
  final val NAME_FIELD_NUMBER = 3
}
