package org.zalando.komang.api.controller

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import org.zalando.komang.api.json.KomangJsonSupport
import org.zalando.komang.service.KomangService

import scala.concurrent.ExecutionContext

trait KomangController extends KomangJsonSupport {
  def komangService: KomangService

  implicit def ec: ExecutionContext

  def getApplications: Route = {
    complete(komangService.listApplications)
  }
}
