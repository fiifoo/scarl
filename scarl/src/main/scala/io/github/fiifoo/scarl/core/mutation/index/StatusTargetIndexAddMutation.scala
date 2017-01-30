package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, StatusId}

case class StatusTargetIndexAddMutation(status: StatusId, target: EntityId) {
  type Index = Map[EntityId, List[StatusId]]

  def apply(index: Index): Index = {
    val statuses = if (index.isDefinedAt(target)) {
      status :: index(target)
    } else {
      List(status)
    }

    index + (target -> statuses)
  }
}
