package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.action.{DisplaceAction, MoveAction, PassAction, UseDoorAction}
import io.github.fiifoo.scarl.ai.tactic.{AttackTactic, FollowTactic}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureComrades, getLocationEntities, getLocationWaypoint}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Obstacle.getClosedDoor
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint
import io.github.fiifoo.scarl.core.geometry._

object Utils {

  private val distance = Distance.chebyshev _

  def findPartyEnemy(s: State, creature: CreatureId): Option[Creature] = {
    getCreatureComrades(s)(creature) flatMap s.tactics.get collectFirst {
      case tactic: AttackTactic if tactic.target(s).isDefined => tactic.target(s).get
    }
  }

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

  def findVisibleEnemy(s: State, actor: CreatureId): Option[Creature] = {
    val creature = actor(s)
    val los = Los(s) _

    val factions = creature.faction(s).enemies
    val range = creature.stats.sight.range
    val enemies = findTargets(s, creature.id, factions, range)

    enemies find (candidate => {
      val line = Line(creature.location, candidate.location)

      los(line)
    })
  }

  def follow(s: State, actor: CreatureId)(target: Creature): Option[Result] = {
    travel(s, actor, target.location, displace = true) map ((FollowTactic(SafeCreatureId(target.id)), _))
  }

  def isEnemy(s: State, creature: CreatureId): CreatureId => Boolean = {
    val factions = creature(s).faction(s).enemies

    enemy => factions contains enemy(s).faction
  }

  def move(s: State,
           actor: CreatureId,
           to: Location,
           displace: Boolean = false,
           wait: Boolean = false
          ): Option[Action] = {
    val from = actor(s).location

    Path(s)(from, to) map (path => {
      MoveAction(path.head)
    }) orElse {
      val keys = s.keys.getOrElse(actor, Set())

      Path.calc(Obstacle.has(Obstacle.travel(s, keys)))(from, to) flatMap (path => {
        val location = path.head

        (getLocationEntities(s)(location) collectFirst {
          case creature: CreatureId => if (displace && !isEnemy(s, actor)(creature)) {
            Some(DisplaceAction(creature))
          } else if (wait) {
            Some(PassAction)
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

  def travel(s: State,
             actor: CreatureId,
             to: Location,
             displace: Boolean = false,
             wait: Boolean = false
            ): Option[Action] = {
    getWaypointPath(s, actor(s).location, to) flatMap (path => {
      val destination = if (path.size > 2) {
        path(1)
      } else {
        to
      }

      move(s, actor, destination, displace, wait)
    })
  }

  private def getWaypointPath(s: State, from: Location, to: Location): Option[Vector[Waypoint]] = {
    getLocationWaypoint(s)(from) flatMap (from => {
      getLocationWaypoint(s)(to) flatMap (to => {
        WaypointPath(s)(from, to)
      })
    })
  }

  private def inRange(from: Location, target: Creature, range: Int): Boolean = {
    distance(from, target.location) <= range
  }

}
