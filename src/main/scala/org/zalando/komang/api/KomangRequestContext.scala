package org.zalando.komang.api

import akka.http.scaladsl.server.RequestContext

case class KomangRequestContext(
    requestContext: RequestContext,
    pretty: Option[Boolean]
) extends WrappedRequestContext[KomangRequestContext](requestContext) {
  override def wrap(requestContext: RequestContext): KomangRequestContext =
    KomangRequestContext(requestContext, None)
}
