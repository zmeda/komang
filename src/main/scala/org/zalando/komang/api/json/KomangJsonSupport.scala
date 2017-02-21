package org.zalando.komang.api.json

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.zalando.komang.api.ApiModel.Error._
import org.zalando.komang.api.json.SprayJsonReadSupport._
import org.zalando.komang.api.ApiModel._
import org.zalando.komang.api.KomangRequestContext
import org.zalando.komang.model.Model._
import spray.json._

trait KomangJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object ApplicationFormat extends RootJsonFormat[Application] {
    override def write(application: Application): JsValue = {
      val applicationId = Some("application_id" -> application.applicationId.toJson)
      val name = Some("name" -> application.name.toJson)
      JsObject(collectSome(applicationId, name).toMap)
    }

    override def read(json: JsValue): Application = {
      val obj = json.asJsObject
      val applicationId = (obj \ "application_id").convertTo[ApplicationId]
      val name = (obj \ "name").convertTo[ApplicationName]
      Application(applicationId, name)
    }
  }

  implicit object ApplicationDraftFormat extends RootJsonFormat[ApplicationDraft] {
    override def write(applicationDraft: ApplicationDraft): JsValue = {
      val name = Some("name" -> applicationDraft.name.toJson)
      JsObject(collectSome(name).toMap)
    }

    override def read(json: JsValue): ApplicationDraft = {
      val obj = json.asJsObject
      val name = (obj \ "name").convertTo[ApplicationName]
      ApplicationDraft(name)
    }
  }

  implicit object ApplicationUpdateFormat extends RootJsonFormat[ApplicationUpdate] {
    override def write(applicationUpdate: ApplicationUpdate): JsValue = {
      val name = Some("name" -> applicationUpdate.name.toJson)
      JsObject(collectSome(name).toMap)
    }

    override def read(json: JsValue): ApplicationUpdate = {
      val obj = json.asJsObject
      val name = (obj \ "name").convertTo[ApplicationName]
      ApplicationUpdate(name)
    }
  }

  implicit object ApplicationIdFormat extends JsonFormat[ApplicationId] {
    override def write(applicationId: ApplicationId): JsValue =
      JsString(applicationId.value.toString)

    override def read(json: JsValue): ApplicationId =
      json match {
        case JsString(str) =>
          parseUuidString(str) match {
            case None => deserializationError(s"Expected UUID but got $str")
            case Some(uuid) => ApplicationId(uuid)
          }
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ApplicationNameFormat extends JsonFormat[ApplicationName] {
    override def write(applicationName: ApplicationName): JsValue =
      JsString(applicationName.value.toString)

    override def read(json: JsValue): ApplicationName =
      json match {
        case JsString(str) =>
          ApplicationName(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ProfileDraftFormat extends RootJsonFormat[ProfileDraft] {
    override def write(profileDraft: ProfileDraft): JsValue = {
      val name = Some("name" -> profileDraft.name.toJson)
      JsObject(collectSome(name).toMap)
    }

    override def read(json: JsValue): ProfileDraft = {
      val obj = json.asJsObject
      val name = (obj \ "name").convertTo[ProfileName]
      ProfileDraft(name)
    }
  }

  implicit object ProfileUpdateFormat extends RootJsonFormat[ProfileUpdate] {
    override def write(profileUpdate: ProfileUpdate): JsValue = {
      val name = Some("name" -> profileUpdate.name.toJson)
      JsObject(collectSome(name).toMap)
    }

    override def read(json: JsValue): ProfileUpdate = {
      val obj = json.asJsObject
      val name = (obj \ "name").convertTo[ProfileName]
      ProfileUpdate(name)
    }
  }

  implicit object ProfileNameFormat extends JsonFormat[ProfileName] {
    override def write(profileName: ProfileName): JsValue =
      JsString(profileName.value.toString)

    override def read(json: JsValue): ProfileName =
      json match {
        case JsString(str) =>
          ProfileName(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ProfileFormat extends RootJsonFormat[Profile] {
    override def write(profile: Profile): JsValue = {
      val profileId = Some("profile_id" -> profile.profileId.toJson)
      val name = Some("name" -> profile.name.toJson)
      JsObject(collectSome(profileId, name).toMap)
    }

    override def read(json: JsValue): Profile = {
      val obj = json.asJsObject
      val profileId = (obj \ "profile_id").convertTo[ProfileId]
      val name = (obj \ "name").convertTo[ProfileName]
      Profile(profileId, name)
    }
  }

  implicit object ProfileIdFormat extends JsonFormat[ProfileId] {
    override def write(profileId: ProfileId): JsValue =
      JsString(profileId.value.toString)

    override def read(json: JsValue): ProfileId =
      json match {
        case JsString(str) =>
          parseUuidString(str) match {
            case None => deserializationError(s"Expected UUID but got $str")
            case Some(uuid) => ProfileId(uuid)
          }
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit def configsFormat(implicit context: KomangRequestContext) = new RootJsonFormat[Vector[Config]] {
    override def write(configs: Vector[Config]): JsValue = {
      context.pretty match {
        case Some(true) => JsObject(configs.map(c => c.name.value -> c.value.toJson).toMap)
        case _ => JsArray(configs.map(_.toJson))
      }
    }

    override def read(json: JsValue): Vector[Config] = {
      json match {
        case JsArray(elements) =>
          elements.map(_.convertTo[Config])
      }
    }
  }

  implicit object ConfigFormat extends RootJsonFormat[Config] {
    override def write(config: Config): JsValue = {
      val profileId = Some("config_id" -> config.configId.toJson)
      val name = Some("name" -> config.name.toJson)
      val `type` = Some("type" -> config.`type`.toJson)
      val value = Some("value" -> config.value.toJson)
      JsObject(collectSome(profileId, name, `type`, value).toMap)
    }

    override def read(json: JsValue): Config = {
      val obj = json.asJsObject
      val configId = (obj \ "config_id").convertTo[ConfigId]
      val name = (obj \ "name").convertTo[ConfigName]
      val `type` = (obj \ "type").convertTo[ConfigType]
      val value = (obj \ "value").convertTo[ConfigValue]
      Config(configId, name, `type`, value)
    }
  }

  implicit object ConfigIdFormat extends JsonFormat[ConfigId] {
    override def write(configId: ConfigId): JsValue =
      JsString(configId.value.toString)

    override def read(json: JsValue): ConfigId =
      json match {
        case JsString(str) =>
          parseUuidString(str) match {
            case None => deserializationError(s"Expected UUID but got $str")
            case Some(uuid) => ConfigId(uuid)
          }
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ConfigNameFormat extends JsonFormat[ConfigName] {
    override def write(configName: ConfigName): JsValue =
      JsString(configName.value.toString)

    override def read(json: JsValue): ConfigName =
      json match {
        case JsString(str) =>
          ConfigName(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ConfigTypeFormat extends JsonFormat[ConfigType] {
    override def write(configType: ConfigType): JsValue =
      JsString(configType.value.toString)

    override def read(json: JsValue): ConfigType =
      json match {
        case JsString(str) =>
          ConfigType(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ConfigValueFormat extends JsonFormat[ConfigValue] {
    override def write(configValue: ConfigValue): JsValue =
      JsString(configValue.value.toString)

    override def read(json: JsValue): ConfigValue =
      json match {
        case JsString(str) =>
          ConfigValue(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ConfigDraftFormat extends RootJsonFormat[ConfigDraft] {
    override def write(configDraft: ConfigDraft): JsValue = {
      val name = Some("name" -> configDraft.name.toJson)
      val `type` = Some("type" -> configDraft.`type`.toJson)
      val value = Some("value" -> configDraft.value.toJson)
      JsObject(collectSome(name, `type`, value).toMap)
    }

    override def read(json: JsValue): ConfigDraft = {
      val obj = json.asJsObject
      val name = (obj \ "name").convertTo[ConfigName]
      val `type` = (obj \ "type").convertTo[ConfigType]
      val value = (obj \ "value").convertTo[ConfigValue]
      ConfigDraft(name, `type`, value)
    }
  }

  implicit object ConfigUpdateFormat extends RootJsonFormat[ConfigUpdate] {
    override def write(configUpdate: ConfigUpdate): JsValue = {
      val name = configUpdate.name.map("name" -> _.toJson)
      val `type` = configUpdate.`type`.map("type" -> _.toJson)
      val value = configUpdate.value.map("value" -> _.toJson)
      JsObject(collectSome(name, `type`, value).toMap)
    }

    override def read(json: JsValue): ConfigUpdate = {
      val obj = json.asJsObject
      val name = (obj \? "name").map(_.convertTo[ConfigName])
      val `type` = (obj \? "type").map(_.convertTo[ConfigType])
      val value = (obj \? "value").map(_.convertTo[ConfigValue])
      ConfigUpdate(name, `type`, value)
    }
  }

  implicit object ErrorFormat extends RootJsonFormat[Error] {
    override def write(error: Error): JsValue = {
      val errorId = Some("error_id" -> error.errorId.toJson)
      val code = Some("code" -> error.code.toJson)
      val internalCode = Some("internal_code" -> error.internalCode.toJson)
      val description = Some("description" -> error.description.toJson)
      JsObject(collectSome(errorId, code, internalCode, description).toMap)
    }

    override def read(json: JsValue): Error = {
      val obj = json.asJsObject
      val errorId = (obj \ "error_id").convertTo[ErrorId]
      val code = (obj \ "code").convertTo[ErrorCode]
      val internalCode = (obj \ "internal_code").convertTo[InternalErrorCode]
      val description = (obj \ "description").convertTo[ErrorDescription]
      Error(errorId, code, internalCode, description)
    }
  }

  implicit object ErrorIdFormat extends JsonFormat[ErrorId] {
    override def write(errorId: ErrorId): JsValue =
      JsString(errorId.value.toString)

    override def read(json: JsValue): ErrorId =
      json match {
        case JsString(str) =>
          parseUuidString(str) match {
            case None => deserializationError(s"Expected UUID but got $str")
            case Some(uuid) => ErrorId(uuid)
          }
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ErrorCodeFormat extends JsonFormat[ErrorCode] {
    override def write(code: ErrorCode): JsValue =
      JsString(code.value.toString)

    override def read(json: JsValue): ErrorCode =
      json match {
        case JsNumber(number) =>
          ErrorCode(number.toInt)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object InternalErrorCodeFormat extends JsonFormat[InternalErrorCode] {
    override def write(internalCode: InternalErrorCode): JsValue =
      JsString(internalCode.value.toString)

    override def read(json: JsValue): InternalErrorCode =
      json match {
        case JsString(str) =>
          InternalErrorCode(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  implicit object ErrorDescriptionFormat extends JsonFormat[ErrorDescription] {
    override def write(description: ErrorDescription): JsValue =
      JsString(description.value.toString)

    override def read(json: JsValue): ErrorDescription =
      json match {
        case JsString(str) =>
          ErrorDescription(str)
        case x => deserializationError(s"Expected type String but got $x")
      }
  }

  private def parseUuidString(token: String): Option[UUID] = {
    if (token.length != 36) None
    else
      try Some(UUID.fromString(token))
      catch {
        case _: IllegalArgumentException => None
      }
  }

  private def collectSome[A](opts: Option[A]*): List[A] =
    (opts collect { case Some(field) => field }).toList
}
