package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import fi.fiifoo.scarl.core.mutation.NewEntityMutation

case class TestActorStatusEffect(target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    val status = TestActiveStatus(ActiveStatusId(s.nextEntityId), s.tick, target)

    EffectResult(NewEntityMutation(status))
  }
}
