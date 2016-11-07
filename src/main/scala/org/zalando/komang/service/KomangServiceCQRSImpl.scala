package org.zalando.komang.service

import akka.actor.ActorRef
import akka.pattern.ask
import org.zalando.komang.model.Commands
import org.zalando.komang.model.Model.Application

import scala.concurrent.Future

class KomangServiceCQRSImpl(persistentActor: ActorRef) extends KomangService {
  override def listApplications: Future[Vector[Application]] = ???

  override def createApplication(application: Application): Future[Application] = persistentActor ? Commands.CreateApplication(application) map {
    case response: Commands.CreateApplicationResponse => response.application
   }
}
