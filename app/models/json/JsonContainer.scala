package models.json

import io.github.fiifoo.scarl.core.entity.{Container, ContainerId}
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonContainer {

  import JsonBase.integerIdFormat

  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val safeCreatureIdFormat = JsonCreature.safeCreatureIdFormat

  lazy implicit val containerIdFormat: Format[ContainerId] = integerIdFormat(_.value, ContainerId.apply)

  lazy val containerFormat: Format[Container] = Json.format[Container]
}
