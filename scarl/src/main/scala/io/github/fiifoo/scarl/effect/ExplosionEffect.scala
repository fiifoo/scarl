package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.rule.AttackRule
import io.github.fiifoo.scarl.rule.AttackRule.Result

case class ExplosionEffect(attacker: CreatureId,
                           location: Location,
                           explosive: Explosive,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()
    val rule = AttackRule.explosive(s, random) _

    EffectResult(
      RngMutation(rng),
      (targets(s) map attack(rule)).toList
    )
  }

  def targets(s: State): Set[CreatureId] = {
    (Selectors.getLocationEntities(s)(location) collect {
      case c: CreatureId => c
    }) - attacker
  }

  private def attack(rule: (CreatureId, CreatureId) => Result)(target: CreatureId): Effect = {
    val result = rule(attacker, target)

    if (result.hit) {
      HitEffect(attacker, target, result, Some(this))
    } else {
      MissEffect(attacker, target, Some(this))
    }
  }
}
