package org.zalando.komang.api.json

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.zalando.komang.model.Model.{Application, ApplicationId}
import spray.json._
import SprayJsonReadSupport._

trait KomangJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object ApplicationFormat extends RootJsonFormat[Application] {
    override def write(application: Application): JsValue = {
      val applicationId = application.applicationId map ("application_id" -> _.toJson)
      val name = Some("name" -> application.name.toJson)
      JsObject(collectSome(applicationId, name) toMap)
    }

    override def read(json: JsValue): Application = {
      val obj = json.asJsObject
      val applicationId = (obj \? "application_id") map (_.convertTo[ApplicationId])
      val name = (obj \ "name").asString
      Application(applicationId, name)
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
