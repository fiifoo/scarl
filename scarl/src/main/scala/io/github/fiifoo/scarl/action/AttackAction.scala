package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.effect.combat.StrikeEffect

case class AttackAction(target: CreatureId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val stats = getCreatureStats(s)(actor).melee
    val attackEffect = StrikeEffect(actor, target)

    Utils.Attack(s, actor, stats, attackEffect)
  }
}
