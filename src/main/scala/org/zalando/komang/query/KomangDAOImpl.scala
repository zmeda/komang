package org.zalando.komang.query
import org.zalando.komang.model.Model.Application

import scala.concurrent.Future

class KomangDAOImpl extends KomangDAO {
  override def getAllApplications(): Future[Vector[Application]] = ???
}
