package game

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.kind.{CreatureKind, ItemKind}
import io.github.fiifoo.scarl.simulation.{CombatPowerSimulation, EquipmentCombatPowerSimulation}

object Simulate {

  def combatPower(creatures: Iterable[CreatureKind]): CombatPower = {
    CombatPowerSimulation()(combatants(creatures))
  }

  def equipmentCombatPower(items: Iterable[ItemKind], creatures: Iterable[CreatureKind]): CombatPower.Equipment = {
    EquipmentCombatPowerSimulation()(items filter (_.power.isEmpty), combatants(creatures))
  }

  private def combatants(creatures: Iterable[CreatureKind]): Iterable[CreatureKind] = {
    creatures filterNot (_.traits.missile.isDefined)
  }
}
