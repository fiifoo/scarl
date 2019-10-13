package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.effect.creature.condition.RollConditionsEffect
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect
import io.github.fiifoo.scarl.rule.AttackRule

trait AbstractHitEffect extends Effect {
  val target: CreatureId
  val result: AttackRule.Result
  val conditions: List[Condition]
  val location: Location
  val parent: Option[Effect]


  def apply(s: State): EffectResult = {
    val damageEffect = result.damage map (damage => {
      DamageEffect(target, damage, Some(this))
    })

    val eventEffect = target(s).traits.events flatMap (_.hit) map (power => {
      PowerUseEffect(Some(target), target, power, requireResources = false, Some(this))
    })

    val conditionsEffect = if (this.conditions.nonEmpty) {
      Some(RollConditionsEffect(this.target, this.conditions, this.location, Some(this)))
    } else {
      None
    }

    EffectResult(List(
      damageEffect,
      eventEffect,
      conditionsEffect
    ).flatten)
  }
}
