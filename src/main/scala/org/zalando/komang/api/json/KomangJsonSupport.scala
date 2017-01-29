package org.zalando.komang.api.json

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.zalando.komang.api.json.SprayJsonReadSupport._
import org.zalando.komang.api.ApiModel.{ApplicationDraft, ApplicationUpdate, ProfileDraft}
import org.zalando.komang.model.Model.{Application, ApplicationId, ApplicationName, ProfileName}
import spray.json._

trait KomangJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object ApplicationFormat extends RootJsonFormat[Application] {
    override def write(application: Application): JsValue = {
      val applicationId = Some("application_id" -> application.applicationId.toJson)
      val name = Some("name" -> application.name.toJson)
      JsObject(collectSome(applicationId, name) toMap)
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
      JsObject(collectSome(name) toMap)
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
      JsObject(collectSome(name) toMap)
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
      JsObject(collectSome(name) toMap)
    }

    override def read(json: JsValue): ProfileDraft = {
      val obj = json.asJsObject
      val name = (obj \ "name").convertTo[ProfileName]
      ProfileDraft(name)
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
