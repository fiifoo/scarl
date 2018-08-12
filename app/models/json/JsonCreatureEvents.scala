package models.json

import io.github.fiifoo.scarl.core.creature.Events
import play.api.libs.json._

object JsonCreatureEvents {
  lazy private implicit val creaturePowerFormat = JsonPower.creaturePowerFormat

  lazy val creatureEventsFormat: Format[Events] = Json.format
}
