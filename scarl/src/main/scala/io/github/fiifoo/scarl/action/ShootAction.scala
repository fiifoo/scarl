package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.effect.combat.ShootEffect

case class ShootAction(location: Location) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val stats = getCreatureStats(s)(actor).ranged
    val attackEffect = ShootEffect(actor, location)

    Utils.Attack(s, actor, stats, attackEffect)
  }
}
