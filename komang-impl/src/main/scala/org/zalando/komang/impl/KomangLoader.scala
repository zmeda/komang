package org.zalando.komang.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import org.zalando.komang.api.KomangService
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

class KomangLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new KomangApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new KomangApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[KomangService]
  )
}

abstract class KomangApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[KomangService].to(wire[KomangServiceImpl])
  )

  override lazy val jsonSerializerRegistry = KomangSerializerRegistry

  persistentEntityRegistry.register(wire[KomangEntity])
}
