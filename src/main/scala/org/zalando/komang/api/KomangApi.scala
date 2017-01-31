package org.zalando.komang.api

import akka.http.scaladsl.server.Directives._
import org.zalando.komang.api.controller.KomangController
import org.zalando.komang.api.TypedIdPathMatchers._
// format: off
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
        pathEndOrSingleSlash {
          get {
            getApplication(applicationId)
          } ~
          patch {
            updateApplication(applicationId)
          }
        } ~
        pathPrefix("profiles") {
          pathEndOrSingleSlash {
            get {
              getProfiles(applicationId)
            } ~
            post {
              createProfile(applicationId)
            }
          } ~
          pathPrefix(ProfileIdentity) { profileId =>
            pathEndOrSingleSlash {
              get {
                getProfile(applicationId, profileId)
              } ~
              patch {
                updateProfile(applicationId, profileId)
              }
            }
          }
        }
      }
    }
  }
}
// format: on
