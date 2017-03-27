package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, StatusId}

case class StatusTargetIndexRemoveMutation(status: StatusId, target: EntityId) {
  type Index = Map[EntityId, Set[StatusId]]

  def apply(index: Index): Index = {
    val statuses = index(target) - status

    if (statuses.isEmpty) {
      index - target
    } else {
      index + (target -> statuses)
    }
  }
}
