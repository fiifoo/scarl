package models.json

import io.github.fiifoo.scarl.core.entity.{GlobalActor, GlobalActorId}
import play.api.libs.json._

object JsonGlobalActor {

  import JsonBase.integerIdFormat

  lazy implicit val globalActorIdFormat: Format[GlobalActorId] = integerIdFormat(_.value, GlobalActorId.apply)

  lazy val globalActorFormat: Format[GlobalActor] = Json.format[GlobalActor]
}
