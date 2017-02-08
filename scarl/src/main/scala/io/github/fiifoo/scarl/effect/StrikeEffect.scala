package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case class StrikeEffect(attacker: CreatureId, target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    val effect = if (new Random(s.seed).nextBoolean()) {
      HitEffect(attacker, target)
    } else {
      MissEffect(attacker, target)
    }

    EffectResult(effect)
  }
}
