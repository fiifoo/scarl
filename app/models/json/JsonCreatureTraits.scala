package models.json

import io.github.fiifoo.scarl.core.creature.Traits
import play.api.libs.json._

object JsonCreatureTraits {
  lazy private implicit val creatureEventsFormat = JsonCreatureEvents.creatureEventsFormat
  lazy private implicit val creatureMissileFormat = JsonCreatureMissile.creatureMissileFormat

  lazy val creatureTraitsFormat: Format[Traits] = Json.format
}
