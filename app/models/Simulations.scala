package models

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.simulation.CombatPowerSimulation

object Simulations {

  def combatPower(): CombatPower = {
    val combatants = Data.kinds.creatures.values filterNot (_.missile.isDefined)

    CombatPowerSimulation(combatants)
  }

}
