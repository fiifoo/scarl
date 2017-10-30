package io.github.fiifoo.scarl.core.mutation.cache

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.geometry.WaypointNetwork

// TODO: could/should calculate only changed sectors
case class WaypointNetworkCacheMutation() {
  def apply(s: State, cache: WaypointNetwork): WaypointNetwork = {
    WaypointNetwork(s)
  }
}
