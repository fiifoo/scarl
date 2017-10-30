package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.Selectors.{getContainerItems, getTargetStatuses}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{EntityId, WallId}

case class RemovableEntityMutation(id: EntityId) extends Mutation {

  def apply(s: State): State = {
    if (s.tmp.removableEntities contains id) {
      return s
    }

    var result = s.copy(tmp = s.tmp.copy(
      removableEntities = s.tmp.removableEntities + id
    ))

    result = getContainerItems(s)(id).foldLeft(result)((s, item) => {
      RemovableEntityMutation(item)(s)
    })
    result = getTargetStatuses(s)(id).foldLeft(result)((s, status) => {
      RemovableEntityMutation(status)(s)
    })

    result = id match {
      case _: WallId => result.copy(tmp = result.tmp.copy(
        waypointNetworkChanged = true,
      ))
      case _ => result
    }

    result
  }
}
