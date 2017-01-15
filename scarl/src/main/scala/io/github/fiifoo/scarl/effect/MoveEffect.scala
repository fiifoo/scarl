package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class MoveEffect(target: LocatableId, location: Location) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(LocatableLocationMutation(target, location))
  }
}
