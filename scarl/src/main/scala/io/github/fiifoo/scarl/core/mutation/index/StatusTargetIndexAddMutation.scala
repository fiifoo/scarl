package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, StatusId}

case class StatusTargetIndexAddMutation(status: StatusId, target: EntityId) {
  type Index = Map[EntityId, Set[StatusId]]

  def apply(index: Index): Index = {
    val statuses = if (index.isDefinedAt(target)) {
      index(target) + status
    } else {
      Set(status)
    }

    index + (target -> statuses)
  }
}
