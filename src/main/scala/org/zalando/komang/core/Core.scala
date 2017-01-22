package org.zalando.komang.core

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import org.zalando.komang.api.Api
import org.zalando.komang.command._
import org.zalando.komang.model.Model.Application
import org.zalando.komang.model.event._
import org.zalando.komang.query.KomangDAOImpl
import org.zalando.komang.service.{KomangService, KomangServiceCQRSImpl}

import scala.concurrent.ExecutionContext

trait Core extends Api {
  implicit val actorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val komangDAO = new KomangDAOImpl()

  override def komangService: KomangService = new KomangServiceCQRSImpl(komangDAO)

  val readJournal = PersistenceQuery(actorSystem).readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  println("Start reading journal")
  readJournal.allPersistenceIds().map {
    case persistenceId =>
      println(s"pId: $persistenceId")
      readJournal.eventsByPersistenceId(persistenceId).map {
        case EventEnvelope(offset, pId, seqNr, event) =>
          println(s"event: $event")
          event.asInstanceOf[Event] match {
            case ac: ApplicationCreatedEvent =>
              println(s"applicationCreate: $ac")
              komangDAO.createApplication(Application(ac.applicationId, ac.name))
          }
      }
  }
//  readJournal.eventsByTag(tag = "my-tag", offset = Sequence(0L)).mapAsync(1) {
//    case EventEnvelope2(offset, persistenceId, sequenceNr, event) =>
//      println(s"event: $event")
//      event.asInstanceOf[Event] match {
//        case ac: ApplicationCreatedEvent =>
//          println(s"applicationCreate: $ac")
//          komangDAO.createApplication(Application(ac.applicationId, ac.name))
//      }
//    case a =>
//      println(s"a: $a")
//      Future.successful(Done)
//  }
  println("End reading journal")

  Http().bindAndHandle(route, "0.0.0.0", 8080)
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
