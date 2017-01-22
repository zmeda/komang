package org.zalando.komang.command

import akka.persistence.journal.{Tagged, WriteEventAdapter}
import org.zalando.komang.model.event.Event

class TaggingEventAdapter extends WriteEventAdapter {
  override def toJournal(event: Any): Any = event match {
    case e: Event => Tagged(e, Set("my-tag"))
    case _ => event
  }

  override def manifest(event: Any): String = "v1"
}
