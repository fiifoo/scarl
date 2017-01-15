package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.StatusId
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class RemoveStatusEffect(target: StatusId) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(RemovableEntityMutation(target))
  }
}
