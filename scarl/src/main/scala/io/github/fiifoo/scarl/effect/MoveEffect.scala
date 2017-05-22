package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getLocationTriggers
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Obstacle

case class MoveEffect(target: CreatureId,
                      location: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    Obstacle.movement(s)(location) map (obstacle => {
      EffectResult(
        CollideEffect(target, location, obstacle, Some(this))
      )
    }) getOrElse {
      EffectResult(
        LocatableLocationMutation(target, location),
        getTriggerEffects(s)
      )
    }
  }

  private def getTriggerEffects(s: State): List[Effect] = {
    getLocationTriggers(s)(location).toList flatMap (_ (s)(s, target))
  }
}
