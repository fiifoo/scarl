package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.rule.AttackRule.Result

case class HitEffect(attacker: CreatureId,
                     target: CreatureId,
                     result: Result,
                     location: Location,
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
