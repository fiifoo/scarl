package models.json

import io.github.fiifoo.scarl.core.creature.Stats
import play.api.libs.json._

object JsonCreatureStats {
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val consumptionFormat = Json.format[Stats.Consumption]

  lazy private implicit val healthFormat = Json.format[Stats.Health]
  lazy private implicit val energyFormat = Json.format[Stats.Energy]
  lazy private implicit val materialsFormat = Json.format[Stats.Materials]
  lazy private implicit val explosiveFormat = Json.format[Stats.Explosive]
  lazy private implicit val launcherFormat = Json.format[Stats.Launcher]
  lazy private implicit val meleeFormat = Json.format[Stats.Melee]
  lazy private implicit val rangedFormat = Json.format[Stats.Ranged]
  lazy private implicit val sightFormat = Json.format[Stats.Sight]
  lazy private implicit val skillFormat = Json.format[Stats.Skill]

  lazy val creatureStatsFormat: Format[Stats] = Json.format
}
