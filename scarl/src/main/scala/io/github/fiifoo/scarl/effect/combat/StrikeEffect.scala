package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.AttackRule

case class StrikeEffect(attacker: CreatureId,
                        target: CreatureId,
                        parent: Option[Effect] = None
                       ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()
    val result = AttackRule.melee(s, random)(attacker, target)

    val effect = if (result.hit) {
      val conditions = getCreatureStats(s)(this.attacker).melee.conditions

      HitEffect(attacker, target, result, conditions, target(s).location, Some(this))
    } else {
      MissEffect(attacker, target, target(s).location, Some(this))
    }

    EffectResult(
      RngMutation(rng),
      effect
    )
  }
}
