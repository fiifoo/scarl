package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.entity.EntityId

case class RemovableEntityMutation(id: EntityId) extends Mutation {

  def apply(s: State): State = {
    if (s.tmp.removableEntities contains id) {
      return s
    }

    val items = s.index.items.container.getOrElse(id, List())
    val statuses = s.index.statuses.target.getOrElse(id, List())

    s.copy(tmp = s.tmp.copy(
      removableEntities = id :: s.tmp.removableEntities ++ items ++ statuses
    ))
  }
}
