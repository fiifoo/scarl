package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getWaypointCreatures
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.CreatureKind

object Party {

  def find(s: State, kind: CreatureKind, location: Location): Option[Party] = {
    val comrade = s.cache.waypointNetwork.locationWaypoint.get(location) flatMap (waypoint => {
      getWaypointCreatures(s)(waypoint) map (_ (s)) find (nearby => {
        !nearby.solitary &&
          nearby.faction == kind.faction &&
          nearby.behavior == kind.behavior
      })
    })

    comrade map (_.party)
  }
}

case class Party(leader: CreatureId)
