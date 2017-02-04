package org.zalando.komang.core

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.persistence.query.{EventEnvelope2, PersistenceQuery, Sequence}
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.typesafe.scalalogging.LazyLogging
import org.flywaydb.core.Flyway
import org.zalando.komang.api.Api
import org.zalando.komang.command._
import org.zalando.komang.model.Model.{Application, Config, Profile}
import org.zalando.komang.model.event._
import org.zalando.komang.query.{ConfigSupport, KomangDAOImpl}
import org.zalando.komang.service.{KomangService, KomangServiceCQRSImpl}

import concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.control.NonFatal

trait Core extends Api with ConfigSupport with LazyLogging {
  implicit val actorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val flyway = new Flyway
  flyway.setDataSource(
    h2mem.getString("url"),
    null,
    null
  )
  flyway.setLocations("db.migration")
  flyway.migrate()

  val komangDAO = new KomangDAOImpl()

  override def komangService: KomangService = new KomangServiceCQRSImpl(komangDAO)

  val readJournal = PersistenceQuery(actorSystem).readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  logger.info("Start reading journal")

  readJournal
    .eventsByTag(tag = "my-tag", offset = Sequence(0L))
    .mapAsync(1) {
      case EventEnvelope2(offset, persistenceId, sequenceNr, event) =>
        logger.info(
          s"event from journal - event: $event; offset: $offset; persistenceId: $persistenceId; sequenceNr: $sequenceNr")
        event.asInstanceOf[Event] match {
          case ac: ApplicationCreatedEvent =>
            logger.info(s"applicationCreated: $ac")
            komangDAO.createApplication(Application(ac.applicationId, ac.name))
          case au: ApplicationUpdatedEvent =>
            logger.info(s"applicationUpdated: $au")
            komangDAO.updateApplication(Application(au.applicationId, au.name))
          case pc: ProfileCreatedEvent =>
            logger.info(s"profileCreated: $pc")
            komangDAO.createProfile(pc.applicationId, Profile(pc.profileId, pc.name))
          case pu: ProfileUpdatedEvent =>
            logger.info(s"profileUpdated: $pu")
            komangDAO.updateProfile(pu.applicationId, Profile(pu.profileId, pu.name))
          case cc: ConfigCreatedEvent =>
            logger.info(s"configCreated: $cc")
            komangDAO.createConfig(cc.profileId, Config(cc.configId, cc.name, cc.`type`, cc.value))
          case cnu: ConfigNameUpdatedEvent =>
            logger.info(s"configNameUpdated: $cnu")
            komangDAO.updateConfigName(cnu.profileId, cnu.configId, cnu.name)
          case ctu: ConfigTypeUpdatedEvent =>
            logger.info(s"configTypeUpdated: $ctu")
            komangDAO.updateConfigType(ctu.profileId, ctu.configId, ctu.`type`)
          case cvu: ConfigValueUpdatedEvent =>
            logger.info(s"configValueUpdated: $cvu")
            komangDAO.updateConfigValue(cvu.profileId, cvu.configId, cvu.value)
        }
      case a =>
        logger.info(s"We received something else from journal: $a")
        Future.successful(Done)
    }
    .runWith(Sink.ignore)
    .recover {
      case NonFatal(e) => logger.error("Read stream failure", e)
    }

  logger.info("End reading journal")

  logger.info("Add shutdown hook")
  sys.addShutdownHook {
    logger.info("Terminating...")
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, 30.seconds)
    logger.info("Terminated... Bye")
  }

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
