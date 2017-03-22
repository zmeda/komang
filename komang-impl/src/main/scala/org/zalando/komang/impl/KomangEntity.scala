package org.zalando.komang.impl

import java.util.UUID

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}
import org.zalando.komang.impl.JsonFormats._

import scala.collection.immutable.Seq

class KomangEntity extends PersistentEntity {
  override type Command = KomangCommand[_]
  override type Event = KomangEvent
  override type State = Option[Application]

  override def initialState: Option[Application] = None

  override def behavior: Behavior = {
    case None => notCreated
    case Some(_) => getApplicationCommand
  }

  private val getApplicationCommand = Actions().onReadOnlyCommand[GetApplication.type, Option[Application]] {
    case (GetApplication, ctx, state) => ctx.reply(state)
  }

  private val notCreated = {
    Actions().onCommand[CreateApplication, Done] {
      case (CreateApplication(application), ctx, _) =>
        ctx.thenPersist(ApplicationCreated(application))(_ => ctx.reply(Done))
    }.onEvent {
      case (ApplicationCreated(application), _) => Some(application)
    }.orElse(getApplicationCommand)
  }
}

sealed trait KomangEvent

case class ApplicationCreated(application: Application) extends KomangEvent

object ApplicationCreated {
  implicit val format: Format[ApplicationCreated] = Json.format
}

sealed trait KomangCommand[R] extends ReplyType[R]

case class CreateApplication(application: Application) extends KomangCommand[Done]

object CreateApplication {
  implicit val format: Format[CreateApplication] = Json.format
}

case object GetApplication extends KomangCommand[Option[Application]] {
  implicit val format: Format[GetApplication.type] = singletonFormat(GetApplication)
}

object KomangSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[GetApplication.type],
    JsonSerializer[ApplicationCreated],
    JsonSerializer[CreateApplication]
  )
}

case class Application(id: UUID, name: String)

object Application {
  implicit val format: Format[Application] = Json.format[Application]
}