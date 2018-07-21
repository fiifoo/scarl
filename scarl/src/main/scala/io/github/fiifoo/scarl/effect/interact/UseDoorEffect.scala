package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.{getItemLocation, getLocationEntities, hasLockKey}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Door
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class UseDoorEffect(user: Option[CreatureId],
                         target: ItemId,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    val item = target(s)

    getItemLocation(s)(target) flatMap (location => {
      item.door map use(s, location, item)
    }) getOrElse {
      EffectResult()
    }
  }

  private def use(s: State, location: Location, item: Item)(door: Door): EffectResult = {
    getObstacle(s, location) map (obstacle => {
      EffectResult(
        BlockedDoorEffect(user, target, obstacle, location, Some(this))
      )
    }) getOrElse {
      item.locked flatMap (lock => {
        user flatMap (user => {
          if (hasLockKey(s)(user)(lock)) {
            None
          } else {
            Some(EffectResult(
              LockedUsableEffect(user, target, location, Some(this))
            ))
          }
        })
      }) getOrElse {
        val opening = !door.open
        val result = door.transformTo(s).toContainer(s, s.idSeq, target(s).container, user)

        EffectResult(
          RemovableEntityMutation(target) :: result.mutations,
          DoorUsedEffect(user, target, opening, location, Some(this))
        )
      }
    }
  }

  private def getObstacle(s: State, location: Location): Option[CreatureId] = {
    getLocationEntities(s)(location) collectFirst {
      case creature: CreatureId => creature
    }
  }
}
