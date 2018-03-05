package models.json

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import play.api.libs.json._

object JsonCombatPower {

  import JsonBase.{mapReads, polymorphicObjectFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  implicitly(mapReads[CreatureKindId, Int])

  lazy val combatPowerWrites: Writes[CombatPower] = Json.writes[CombatPower]

  lazy implicit val categoryFormat: Format[CombatPower.Category] = polymorphicObjectFormat({
    case "CombatPower.Top" => CombatPower.Top
    case "CombatPower.High" => CombatPower.High
    case "CombatPower.Medium" => CombatPower.Medium
    case "CombatPower.Low" => CombatPower.Low
  })
}
