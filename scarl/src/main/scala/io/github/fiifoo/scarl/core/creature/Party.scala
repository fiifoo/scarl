package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.{getLocationWaypoint, getWaypointCreatures}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.CreatureKind

object Party {

  def apply(leader: CreatureId): Party = {
    Party(SafeCreatureId(leader))
  }

  def find(s: State, kind: CreatureKind, location: Location): Option[Party] = {
    val waypoint = getLocationWaypoint(s)(location)
    val waypoints = waypoint map (waypoint => {
      s.cache.waypointNetwork.adjacentWaypoints.getOrElse(waypoint, Set()) + waypoint
    }) getOrElse {
      Set()
    }

    val leaders = waypoints flatMap (waypoint => {
      getWaypointCreatures(s)(waypoint) map (_ (s)) find (nearby => {
        SafeCreatureId(nearby.id) == nearby.party.leader &&
          !nearby.traits.solitary &&
          nearby.faction == kind.faction &&
          (nearby.traits.leader || nearby.behavior == kind.behavior)
      })
    })

    leaders collectFirst {
      case x => x.party
    }
  }
}

case class Party(leader: SafeCreatureId)
