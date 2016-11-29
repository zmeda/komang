package org.zalando.komang.core

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.zalando.komang.api.Api
import org.zalando.komang.command.ApplicationAggregate
import org.zalando.komang.service.{KomangService, KomangServiceCQRSImpl}

import scala.concurrent.ExecutionContext

trait Core extends Api {
  implicit val actorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  Http().bindAndHandle(route, "0.0.0.0", 8080)

  override def komangService: KomangService = new KomangServiceCQRSImpl
}

trait Sharding { this: Core =>

  val orderShardRegion: ActorRef = ClusterSharding(actorSystem).start(
    typeName = "application",
    entityProps = ApplicationAggregate.props,
    settings = ClusterShardingSettings(actorSystem).withStateStoreMode("ddata"),
    extractEntityId = ApplicationAggregate.extractEntityId,
    extractShardId = ApplicationAggregate.extractShardId
  )
}
