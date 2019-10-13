package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationTriggers
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.{CreatureTrailMutation, LocatableLocationMutation}

case class MovedEffect(target: CreatureId,
                       from: Location,
                       to: Location,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(List(
      LocatableLocationMutation(target, to),
      CreatureTrailMutation(target, from)
    ),
      getTriggerEffects(s)
    )
  }

  private def getTriggerEffects(s: State): List[Effect] = {
    if (target(s).traits.flying) {
      List()
    } else {
      getLocationTriggers(s)(to).toList flatMap (_ (s)(s, target))
    }
  }
}
