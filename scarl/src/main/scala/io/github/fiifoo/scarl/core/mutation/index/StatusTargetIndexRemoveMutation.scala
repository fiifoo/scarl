package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, StatusId}

case class StatusTargetIndexRemoveMutation(status: StatusId, target: EntityId) {

  def apply(s: Map[EntityId, List[StatusId]]): Map[EntityId, List[StatusId]] = {
    val statuses = s(target) filter (_ != status)

    if (statuses.isEmpty) {
      s - target
    } else {
      s + (target -> statuses)
    }
  }
}
