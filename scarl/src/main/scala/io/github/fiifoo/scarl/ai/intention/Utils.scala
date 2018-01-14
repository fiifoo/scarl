package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.action.{DisplaceAction, MoveAction, UseDoorAction}
import io.github.fiifoo.scarl.ai.tactic.FollowTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Obstacle.getClosedDoor
import io.github.fiifoo.scarl.core.geometry._

object Utils {

  private val distance = Distance.chebyshev _

  def findTargets(s: State, creature: CreatureId, factions: Set[FactionId], range: Int): List[Creature] = {
    val location = creature(s).location

    val filter = (target: Creature) =>
      target.id != creature && inRange(location, target, range) && target.missile.isEmpty

    val targets = (factions foldLeft Set[Creature]()) ((targets, faction) => {
      val filtered = s.index.factionMembers getOrElse(faction, Set()) map (_ (s)) filter filter

      targets ++ filtered
    })

    targets
      .toList
      .sortWith((a, b) => distance(location, a.location) < distance(location, b.location))
  }

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

  def follow(s: State, actor: CreatureId)(target: Creature): Option[Result] = {
    if (WaypointNetwork.isNearbyLocation(s, actor(s).location, target.location)) {
      Utils.move(s, actor, target.location, displace = true) map ((FollowTactic(SafeCreatureId(target.id)), _))
    } else {
      None
    }
  }

  private def inRange(from: Location, target: Creature, range: Int): Boolean = {
    distance(from, target.location) <= range
  }

}
