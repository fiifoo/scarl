package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getLocationTriggers
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Obstacle

case class MoveEffect(target: CreatureId,
                      from: Location,
                      to: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    Obstacle.movement(s)(to) map (obstacle => {
      EffectResult(
        CollideEffect(target, to, obstacle, Some(this))
      )
    }) getOrElse {
      EffectResult(
        LocatableLocationMutation(target, to),
        getTriggerEffects(s)
      )
    }
  }

  private def getTriggerEffects(s: State): List[Effect] = {
    if (target(s).flying) {
      List()
    } else {
      getLocationTriggers(s)(to).toList flatMap (_ (s)(s, target))
    }
  }
}
