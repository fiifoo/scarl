package models.json

import io.github.fiifoo.scarl.core.creature.Stats
import play.api.libs.json._

object JsonCreatureStats {
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat

  lazy private implicit val explosiveFormat = Json.format[Stats.Explosive]
  lazy private implicit val meleeFormat = Json.format[Stats.Melee]
  lazy private implicit val missileLauncherFormat = Json.format[Stats.MissileLauncher]
  lazy private implicit val rangedFormat = Json.format[Stats.Ranged]
  lazy private implicit val sightFormat = Json.format[Stats.Sight]

  lazy val creatureStatsFormat: Format[Stats] = Json.format
}