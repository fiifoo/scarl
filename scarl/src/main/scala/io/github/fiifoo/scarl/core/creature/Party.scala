package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.kind.CreatureKind

object Party {

  def find(s: State, kind: CreatureKind, location: Location): Option[Party] = {
    val comrade = WaypointNetwork.nearbyCreatures(s, location) map (_ (s)) find (nearby => {
      !nearby.solitary &&
        nearby.faction == kind.faction &&
        nearby.behavior == kind.behavior
    })

    comrade map (_.party)
  }
}

case class Party(leader: CreatureId)
