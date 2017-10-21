package models.json

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import play.api.libs.json._

object JsonCombatPower {

  import JsonBase.mapReads

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  implicitly(mapReads[CreatureKindId, Int])

  lazy val combatPowerWrites: Writes[CombatPower] = Json.writes[CombatPower]
}
