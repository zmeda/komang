package org.zalando.komang.api

import akka.http.scaladsl.server.PathMatcher1
import akka.http.scaladsl.server.PathMatchers.JavaUUID
import org.zalando.komang.model.Model.{ApplicationId, ProfileId}

object TypedIdPathMatchers {
  val ApplicationIdentity: PathMatcher1[ApplicationId] = JavaUUID.map(ApplicationId)

  val ProfileIdentity: PathMatcher1[ProfileId] = JavaUUID.map(ProfileId)
}
