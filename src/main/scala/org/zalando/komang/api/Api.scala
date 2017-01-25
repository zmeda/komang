package org.zalando.komang.api

trait Api extends HealthCheckApi with KomangApi with CustomErrorHandler {
  val route =
    healthCheckRoute ~
      komangRoute
}
