package game

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.kind.CreatureKind
import io.github.fiifoo.scarl.simulation.CombatPowerSimulation

object Simulations {

  def combatPower(creatures: Iterable[CreatureKind]): CombatPower = {
    val combatants = creatures filterNot (_.missile.isDefined)

    CombatPowerSimulation(combatants)
  }

}
