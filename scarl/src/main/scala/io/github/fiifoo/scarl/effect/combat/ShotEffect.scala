package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId, Signal}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.effect.area.SignalEffect
import io.github.fiifoo.scarl.rule.AttackRule

case class ShotEffect(attacker: CreatureId,
                      from: Location,
                      to: Location,
                      target: Option[EntityId],
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    target collect {
      case target: CreatureId => attackResult(s, target)
      case obstacle: EntityId => badShotResult(Some(obstacle))
    } getOrElse {
      badShotResult()
    }
  }

  private def attackResult(s: State, target: CreatureId): EffectResult = {
    val (random, rng) = s.rng()
    val result = AttackRule.ranged(s, random)(attacker, target)

    val effect = if (result.hit) {
      val conditions = getCreatureStats(s)(this.attacker).ranged.conditions

      HitEffect(attacker, target, result, conditions, target(s).location, Some(this))
    } else {
      MissEffect(attacker, target, target(s).location, Some(this))
    }

    EffectResult(
      RngMutation(rng),
      List(
        effect,
        signalEffect
      )
    )
  }

  private def badShotResult(obstacle: Option[EntityId] = None): EffectResult = {
    EffectResult(List(
      BadShotEffect(attacker, obstacle, Some(this)),
      signalEffect
    ))
  }

  private def signalEffect = SignalEffect(Signal.NoiseSignal, this.from, Signal.Weak)
}
