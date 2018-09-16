package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.mutation.RngMutation

case class MaybeEffect(effect: Effect, probability: Int) extends Effect {
  val parent: Option[Effect] = None

  def apply(s: State): EffectResult = {
    val (roll, rng) = s.rng.nextInt(100)

    if (roll + 1 <= probability) {
      EffectResult(RngMutation(rng), effect)
    } else {
      EffectResult(RngMutation(rng))
    }
  }
}
