package org.zalando.komang.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait KomangService extends Service {
  def createApplication: ServiceCall[Application, Application]

  def getApplication(id: UUID): ServiceCall[NotUsed, Application]

  override final def descriptor = {
    named("komangApp").withCalls(
      restCall(Method.POST, "/applications", createApplication),
      restCall(Method.GET, "/applications/:id", getApplication _)
    ).withAutoAcl(true)
  }
}

case class Application(id: Option[UUID], name: String)

object Application {
  implicit val format: Format[Application] = Json.format[Application]
}