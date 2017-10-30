package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.mutation.cache.WaypointNetworkCacheMutation

case class FinalizeTickMutation() extends Mutation {

  def apply(s: State): State = {
    var next = s

    if (next.tmp.removableEntities.nonEmpty) {
      next = RemoveEntitiesMutation()(next)
    }
    if (next.tmp.waypointNetworkChanged) {
      next = next.copy(
        cache = next.cache.copy(
          waypointNetwork = WaypointNetworkCacheMutation()(next, next.cache.waypointNetwork)
        ),
        tmp = next.tmp.copy(
          waypointNetworkChanged = false,
        ),
      )
    }

    next
  }
}
