package org.zalando.komang.api.controller

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import org.zalando.komang.api.json.KomangJsonSupport
import org.zalando.komang.api.ApiModel.{
  ApplicationDraft,
  ApplicationNotFoundException,
  ApplicationUpdate,
  ProfileDraft
}
import org.zalando.komang.model.Model.ApplicationId
import org.zalando.komang.service.KomangService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait KomangController extends KomangJsonSupport {
  def komangService: KomangService

  implicit def ec: ExecutionContext

  def getApplications: Route = {
    complete(komangService.listApplications)
  }

  def getApplication(applicationId: ApplicationId): Route = {
    complete {
      komangService.findApplication(applicationId) map {
        _.getOrElse(throw new ApplicationNotFoundException(applicationId))
      }
    }
  }

  def createApplication: Route = {
    extractRequest { request =>
      entity(as[ApplicationDraft]) { applicationDraft =>
        onComplete(komangService.createApplication(applicationDraft)) {
          case Success(applicationId) => {
            respondWithHeader(Location(s"${request.uri}/${applicationId.value}")) {
              complete(
                HttpResponse(status = StatusCodes.Created, entity = HttpEntity.empty(request.entity.contentType)))
            }
          }
          case Failure(ex) =>
            failWith(ex)
        }
      }
    }
  }

  def updateApplication(applicationId: ApplicationId): Route = {
    entity(as[ApplicationUpdate]) { applicationUpdate =>
      complete(komangService.updateApplication(applicationId, applicationUpdate))
    }
  }

  def createProfile(applicationId: ApplicationId): Route = {
    extractRequest { request =>
      entity(as[ProfileDraft]) { profileDraft =>
        onComplete(komangService.createProfile(applicationId, profileDraft)) {
          case Success((applicationId, profileId)) => {
            respondWithHeader(Location(s"${request.uri}/${applicationId.value}/profiles/${profileId.value}")) {
              complete(
                HttpResponse(status = StatusCodes.Created, entity = HttpEntity.empty(request.entity.contentType)))
            }
          }
          case Failure(ex) =>
            failWith(ex)
        }
      }
    }
  }
}
