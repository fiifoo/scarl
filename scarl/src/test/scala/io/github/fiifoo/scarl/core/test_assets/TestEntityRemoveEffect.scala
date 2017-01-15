package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.EntityId
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class TestEntityRemoveEffect(target: EntityId) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(RemovableEntityMutation(target))
  }
}
