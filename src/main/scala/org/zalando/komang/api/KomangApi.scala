package org.zalando.komang.api

import akka.http.scaladsl.server.Directives._
import org.zalando.komang.api.controller.KomangController
import org.zalando.komang.api.TypedIdPathMatchers._

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
      } ~
        pathPrefix(ApplicationIdentity) { applicationId =>
          get {
            getApplication(applicationId)
          } ~
            patch {
              updateApplication(applicationId)
            }
        }
    }
  }
}
