package org.zalando.komang.api

import akka.http.scaladsl.server.Directives._

trait Api extends HealthCheckApi with KomangApi {
  val route =
    healthCheckRoute ~
    komangRoute
}
