package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation

case class TestActorStatusEffect(target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    val status = TestActiveStatus(ActiveStatusId(s.nextEntityId), s.tick, target)

    EffectResult(NewEntityMutation(status))
  }
}
