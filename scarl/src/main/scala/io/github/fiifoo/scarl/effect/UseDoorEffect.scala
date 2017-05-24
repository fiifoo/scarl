package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, EntityId, ItemId}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

case class UseDoorEffect(user: CreatureId,
                         target: ItemId,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    val container = target(s).container

    getLocation(s, container) flatMap (location => {
      getObstacle(s, location) map (obstacle => {
        EffectResult(
          DoorBlockedEffect(user, target, obstacle, Some(this))
        )
      }) orElse {
        target(s).door map (door => {
          val opening = !door.open
          val next = door.transformTo(s)(s, container)

          EffectResult(
            List(
              NewEntityMutation(next),
              RemovableEntityMutation(target)
            ),
            List(
              DoorUsedEffect(user, target, opening, Some(this))
            ))
        })
      }
    }) getOrElse EffectResult()
  }

  private def getLocation(s: State, container: EntityId): Option[Location] = {
    container match {
      case container: ContainerId => Some(container(s).location)
      case _ => None
    }
  }

  private def getObstacle(s: State, location: Location): Option[CreatureId] = {
    Selectors.getLocationEntities(s)(location) collectFirst {
      case creature: CreatureId => creature
    }
  }
}
