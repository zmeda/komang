package org.zalando.komang.core

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.zalando.komang.api.Api
import org.zalando.komang.service.{KomangService, KomangServiceCQRSImpl, KomangServiceMockImpl}

import scala.concurrent.ExecutionContext

trait Core extends Api {
  implicit val actorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  Http().bindAndHandle(route, "0.0.0.0", 8080)

  override def komangService: KomangService = new KomangServiceCQRSImpl
}
