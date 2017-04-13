package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.rule.AttackRule.Result

case class HitEffect(attacker: CreatureId,
                     target: CreatureId,
                     result: Result,
                     parent: Option[Effect] = None
                    ) extends Effect {

  def apply(s: State): EffectResult = {
    result.damage map { damage =>
      EffectResult(
        DamageEffect(target, damage, Some(this))
      )
    } getOrElse EffectResult()
  }
}
