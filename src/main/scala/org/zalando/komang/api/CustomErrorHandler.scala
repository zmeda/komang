package org.zalando.komang.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, ExceptionHandler, RejectionHandler}
import org.zalando.komang.api.ApiModel.{ApplicationNotFoundException, ProfileNotFoundException}

import scala.util.control.NonFatal

trait CustomErrorHandler extends Directives {
  implicit def rootExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ApplicationNotFoundException(aId) =>
        extractLog { logger => ctx =>
          logger.error(s"Application with id {} does not exist", aId.value.toString)
          ctx.complete((NotFound, s"Application with id $aId not found"))
        }
      case ProfileNotFoundException(aId, pId) =>
        extractLog { logger => ctx =>
          logger.error(s"Profile with id {} does not exist for application with id {}",
                       pId.value.toString,
                       aId.value.toString)
          ctx.complete((NotFound, s"Profile with id $pId not found for application with id $aId"))
        }
      case NonFatal(e) =>
        extractLog { logger => ctx =>
          val uri = ctx.request.uri.toString()
          logger.error(s"Request to $uri could not be handled normally: {}", e.getMessage)
          ctx.complete((InternalServerError, s"Server can't fulfill request error for uri : $uri"))
        }
    }

  implicit def rootRejectionHandler: RejectionHandler =
    RejectionHandler
      .newBuilder()
      .handleNotFound {
        extractLog { logger => ctx =>
          logger.error("Route: {} does not exist.", ctx.request.uri.toString())
          ctx.complete((NotFound, "Not found"))
        }
      }
      .result() withFallback RejectionHandler.default
}
