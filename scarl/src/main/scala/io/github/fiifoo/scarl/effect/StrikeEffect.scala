package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.AttackRule

case class StrikeEffect(attacker: CreatureId, target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    val (result, rng) = AttackRule(s, attacker, target)

    val effect = if (result.hit) {
      HitEffect(attacker, target, result)
    } else {
      MissEffect(attacker, target)
    }

    EffectResult(
      RngMutation(rng),
      effect
    )
  }
}
