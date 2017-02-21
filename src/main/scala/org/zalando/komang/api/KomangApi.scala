package org.zalando.komang.api

import org.zalando.komang.api.controller.KomangController
import org.zalando.komang.api.TypedIdPathMatchers._

// format: off
trait KomangApi extends KomangController with KomangDirectives {
  val komangRoute = {
    injectKomangRequestContext {
    //{
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
              } ~
              pathPrefix("configs") {
                pathEndOrSingleSlash {
                  get {
                    getConfigs(applicationId, profileId)
                  } ~
                  post {
                    createConfig(applicationId, profileId)
                  }
                } ~
                pathPrefix(ConfigIdentity) { configId =>
                  pathEndOrSingleSlash {
                    get {
                      getConfig(applicationId, profileId, configId)
                    } ~
                    patch {
                      updateConfig(applicationId, profileId, configId)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
// format: on
