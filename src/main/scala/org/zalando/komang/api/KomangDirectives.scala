package org.zalando.komang.api

import akka.http.scaladsl.server.{Directive0, Directives}

trait KomangDirectives extends Directives {
  def injectKomangRequestContext: Directive0 =
    mapRequestContext { ctx =>
      KomangRequestContext(ctx, None)
    }
}
