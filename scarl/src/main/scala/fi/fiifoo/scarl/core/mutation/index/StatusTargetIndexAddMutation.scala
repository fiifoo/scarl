package fi.fiifoo.scarl.core.mutation.index

import fi.fiifoo.scarl.core.entity.{EntityId, StatusId}

case class StatusTargetIndexAddMutation(status: StatusId, target: EntityId) {

  def apply(s: Map[EntityId, List[StatusId]]): Map[EntityId, List[StatusId]] = {
    val statuses = if (s.isDefinedAt(target)) {
      status :: s(target)
    } else {
      List(status)
    }

    s + (target -> statuses)
  }
}
