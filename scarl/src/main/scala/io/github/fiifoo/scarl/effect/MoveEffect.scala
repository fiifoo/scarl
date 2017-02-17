package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getLocationTriggers
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LocatableId}
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class MoveEffect(target: LocatableId, location: Location) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(
      LocatableLocationMutation(target, location),
      (target match {
        case creature: CreatureId => getLocationTriggers(s)(location) map (_ (s)(s, creature))
        case _ => List()
      }).flatten
    )
  }
}
