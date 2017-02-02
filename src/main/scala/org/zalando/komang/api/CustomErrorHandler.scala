package org.zalando.komang.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, ExceptionHandler, RejectionHandler}
import org.zalando.komang.api.ApiModel.{ApplicationNotFoundException, ConfigNotFoundException, ProfileNotFoundException}

import scala.util.control.NonFatal

trait CustomErrorHandler extends Directives {
  implicit def rootExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ApplicationNotFoundException(aId) =>
        extractLog { logger => ctx =>
          logger.error(s"An application with id {} does not exist", aId.value.toString)
          ctx.complete((NotFound, s"An application with id $aId not found"))
        }
      case ProfileNotFoundException(aId, pId) =>
        extractLog { logger => ctx =>
          logger.error(s"A profile with id {} does not exist for an application with id {}",
                       pId.value.toString,
                       aId.value.toString)
          ctx.complete((NotFound, s"A profile with id $pId not found for an application with id $aId"))
        }
      case ConfigNotFoundException(aId, pId, cId) =>
        extractLog { logger => ctx =>
          logger.error(s"A config with id {} does not exist for an application with id {} and a profile with id {}",
            cId.value.toString,
            aId.value.toString,
            pId.value.toString)
          ctx.complete((NotFound, s"A config with id $cId not found for an application with id $aId and a profile with id $pId"))
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
