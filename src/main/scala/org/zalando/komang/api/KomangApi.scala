package org.zalando.komang.api

import akka.http.scaladsl.server.Directives._
import org.zalando.komang.api.controller.KomangController

trait KomangApi extends KomangController {
  val komangRoute = {
    pathPrefix("applications") {
      pathEndOrSingleSlash {
        get {
          getApplications
        } ~
          post {
            createApplication
          }
      }
    }
  }
}
