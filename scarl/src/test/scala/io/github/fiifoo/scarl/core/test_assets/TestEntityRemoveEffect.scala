package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.EntityId
import fi.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class TestEntityRemoveEffect(target: EntityId) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(RemovableEntityMutation(target))
  }
}
