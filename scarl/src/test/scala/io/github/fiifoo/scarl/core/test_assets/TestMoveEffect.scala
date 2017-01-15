package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.LocatableId
import fi.fiifoo.scarl.core.mutation.LocatableLocationMutation
import fi.fiifoo.scarl.core.{Location, State}

case class TestMoveEffect(target: LocatableId, location: Location) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(LocatableLocationMutation(target, location))
  }
}
