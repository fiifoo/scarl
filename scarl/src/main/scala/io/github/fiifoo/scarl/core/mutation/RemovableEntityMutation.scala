package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.EntityId

case class RemovableEntityMutation(id: EntityId) extends Mutation {

  def apply(s: State): State = {
    if (s.tmp.removableEntities contains id) {
      return s
    }

    val items = s.index.containerItems.getOrElse(id, List())
    val statuses = s.index.targetStatuses.getOrElse(id, List())

    s.copy(tmp = s.tmp.copy(
      removableEntities = id :: s.tmp.removableEntities ++ items ++ statuses
    ))
  }
}
