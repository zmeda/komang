package org.zalando.komang.query

import com.typesafe.config.ConfigFactory

trait ConfigSupport {
  val conf = ConfigFactory.load()
  val h2mem = conf.getConfig("h2mem")
}
