package org.zalando.komang.api

import akka.event.LoggingAdapter
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes.Redirection
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server.{Rejection, RequestContext, RouteResult}
import akka.http.scaladsl.settings.{ParserSettings, RoutingSettings}
import akka.stream.Materializer

import scala.concurrent.{ExecutionContextExecutor, Future}

abstract class WrappedRequestContext[T <: RequestContext](requestContext: RequestContext) extends RequestContext {

  def wrap(requestContext: RequestContext): T

  override val request: HttpRequest = requestContext.request

  override def reconfigure(
      executionContext: ExecutionContextExecutor,
      materializer: Materializer,
      log: LoggingAdapter,
      settings: RoutingSettings
  ): RequestContext =
    wrap(requestContext.reconfigure(executionContext, materializer, log, settings))

  override implicit def executionContext: ExecutionContextExecutor =
    requestContext.executionContext

  override def log: LoggingAdapter = requestContext.log

  override def withRequest(req: HttpRequest): RequestContext =
    wrap(requestContext.withRequest(req))

  override def mapUnmatchedPath(f: (Path) => Path): RequestContext =
    wrap(requestContext.mapUnmatchedPath(f))

  override def withExecutionContext(ec: ExecutionContextExecutor): RequestContext =
    wrap(requestContext.withExecutionContext(ec))

  override def reject(rejections: Rejection*): Future[RouteResult] =
    requestContext.reject(rejections: _*)

  override def redirect(uri: Uri, redirectionType: Redirection): Future[RouteResult] =
    requestContext.redirect(uri, redirectionType)

  override def withUnmatchedPath(path: Path): RequestContext =
    wrap(requestContext.withUnmatchedPath(path))

  override implicit def materializer: Materializer = requestContext.materializer

  override def parserSettings: ParserSettings = requestContext.parserSettings

  override def complete(obj: ToResponseMarshallable): Future[RouteResult] =
    requestContext.complete(obj)

  override def withAcceptAll: RequestContext = wrap(requestContext.withAcceptAll)

  override def withParserSettings(settings: ParserSettings): RequestContext =
    wrap(requestContext.withParserSettings(settings))

  override def withRoutingSettings(settings: RoutingSettings): RequestContext =
    wrap(requestContext.withRoutingSettings(settings))

  override def fail(error: Throwable): Future[RouteResult] = requestContext.fail(error)

  override def settings: RoutingSettings = requestContext.settings

  override def withLog(log: LoggingAdapter): RequestContext = wrap(requestContext.withLog(log))

  override def mapRequest(f: (HttpRequest) => HttpRequest): RequestContext =
    wrap(requestContext.mapRequest(f))

  override def withMaterializer(materializer: Materializer): RequestContext =
    wrap(requestContext.withMaterializer(materializer))

  override val unmatchedPath: Path = requestContext.unmatchedPath
}
