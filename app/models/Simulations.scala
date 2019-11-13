package models

import game.Simulate
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.kind.CreatureKind
import models.json.JsonCombatPower
import play.api.libs.json.{Format, Json}

case class Simulations(combatPower: CombatPower)

object Simulations {
  def apply(data: Data): Simulations = Simulations(
    simulateCombatPower(data)
  )

  lazy private implicit val combatPowerFormat = JsonCombatPower.combatPowerFormat
  lazy val simulationsFormat: Format[Simulations] = Json.format[Simulations]

  private def simulateCombatPower(data: Data): CombatPower = {
    val main = Simulate.combatPower(getCombatants(data))
    val equipment = Simulate.equipmentCombatPower(data.kinds.items.values, getCombatants(data))

    main.copy(equipment = equipment)
  }

  private def getCombatants(data: Data): Iterable[CreatureKind] = {
    data.kinds.creatures.values filter (x => x.stats.melee.damage > 0 || x.stats.ranged.damage > 0)
  }
}
