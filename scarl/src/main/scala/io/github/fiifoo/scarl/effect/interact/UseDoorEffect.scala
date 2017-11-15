package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.Selectors.{getItemLocation, getLocationEntities}
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class UseDoorEffect(user: Option[CreatureId],
                         target: ItemId,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    getItemLocation(s)(target) flatMap (location => {
      getObstacle(s, location) map (obstacle => {
        EffectResult(
          DoorBlockedEffect(user, target, obstacle, location, Some(this))
        )
      }) orElse {
        target(s).door map (door => {
          val opening = !door.open
          val result = door.transformTo(s).toContainer(s, s.idSeq, target(s).container)

          EffectResult(
            RemovableEntityMutation(target) :: result.mutations,
            List(DoorUsedEffect(user, target, opening, location, Some(this))))
        })
      }
    }) getOrElse EffectResult()
  }

  private def getObstacle(s: State, location: Location): Option[CreatureId] = {
    getLocationEntities(s)(location) collectFirst {
      case creature: CreatureId => creature
    }
  }
}
