package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.{getLocationEntities, getLocationTriggers}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LocatableId, WallId}
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class MoveEffect(target: CreatureId, location: Location) extends Effect {

  def apply(s: State): EffectResult = {
    val obstacle = getObstacle(s)

    if (obstacle.isDefined) {
      EffectResult(
        CollideEffect(target, location, obstacle.get)
      )
    } else {
      EffectResult(
        LocatableLocationMutation(target, location),
        getTriggerEffects(s)
      )
    }
  }

  private def getObstacle(s: State): Option[LocatableId] = {
    getLocationEntities(s)(location) collectFirst {
      case w: WallId => w
      case c: CreatureId => c
    }
  }

  private def getTriggerEffects(s: State): List[Effect] = {
    getLocationTriggers(s)(location) flatMap (_ (s)(s, target))
  }
}
