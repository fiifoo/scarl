package io.github.fiifoo.scarl.core.mutation.cache

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}

case class WaypointNetworkCacheMutation(locations: Set[Location]) {
  def apply(s: State, cache: WaypointNetwork): WaypointNetwork = {
    WaypointNetwork.recalculate(s, cache, locations)
  }
}
