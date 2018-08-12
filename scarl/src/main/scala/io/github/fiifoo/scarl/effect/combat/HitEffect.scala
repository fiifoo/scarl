package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect
import io.github.fiifoo.scarl.rule.AttackRule.Result

case class HitEffect(attacker: CreatureId,
                     target: CreatureId,
                     result: Result,
                     location: Location,
                     parent: Option[Effect] = None
                    ) extends Effect {

  def apply(s: State): EffectResult = {
    val damageEffect = result.damage map (damage => {
      DamageEffect(target, damage, Some(this))
    })

    val eventEffect = target(s).events flatMap (_.hit) map (power => {
      PowerUseEffect(target, target, power, requireResources = false, Some(this))
    })

    EffectResult(List(
      damageEffect,
      eventEffect
    ).flatten)
  }
}
