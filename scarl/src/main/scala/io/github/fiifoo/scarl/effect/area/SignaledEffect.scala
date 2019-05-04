package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Signal
import io.github.fiifoo.scarl.core.mutation.NewSignalMutation

case class SignaledEffect(signal: Signal, parent: Option[Effect] = None) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(NewSignalMutation(signal))
  }
}
