package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{DisplaceAction, MoveAction, UseDoorAction}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.geometry.Obstacle.getClosedDoor
import io.github.fiifoo.scarl.core.geometry.{Location, Obstacle, Path}

object Utils {

  def move(s: State, actor: CreatureId, to: Location, displace: Boolean = false): Option[Action] = {
    val from = actor(s).location

    Path(s)(from, to) map (path => {
      MoveAction(path.head)
    }) orElse {
      val keys = s.keys.getOrElse(actor, Set())

      Path.calc(Obstacle.has(Obstacle.travel(s, keys)))(from, to) flatMap (path => {
        val location = path.head

        (getLocationEntities(s)(location) collectFirst {
          case creature: CreatureId => if (displace) {
            Some(DisplaceAction(creature))
          } else {
            None
          }
        }) orElse {
          getClosedDoor(s)(location) map (door => {
            Some(UseDoorAction(door))
          })
        } getOrElse {
          Some(MoveAction(location))
        }
      })
    }
  }
}
