package org.zalando.komang.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._

trait HealthCheckApi {
  val healthCheckRoute = {
    path("health") {
      get {
        complete((StatusCodes.OK, "alive"))
      }
    }
  }
}
