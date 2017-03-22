package org.zalando.komang.impl

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import org.zalando.komang.api.KomangService
import org.zalando.komang.api

class KomangServiceImpl(registry: PersistentEntityRegistry) extends KomangService {
  override def createApplication = ServerServiceCall { application =>
    val applicationId = UUIDs.timeBased()

    val srvApplication = Application(applicationId, application.name)

    entityRef(applicationId).ask(CreateApplication(srvApplication)).map { _ =>
      convertApplication(srvApplication)
    }
  }

  override def getApplication(id: UUID) = ServerServiceCall { _ =>
    entityRef(id).ask(GetApplication).map {
      case Some(application) => convertApplication(application)
      case None => throw NotFound(s"Application $id not found")
    }
  }

  private def convertApplication(application: Application): api.Application = {
    api.Application(Some(application.id), application.name)
  }

  private def entityRef(applicationId: UUID) = entityRefString(applicationId.toString)

  private def entityRefString(applicationId: String) = registry.refFor[KomangEntity](applicationId)
}
