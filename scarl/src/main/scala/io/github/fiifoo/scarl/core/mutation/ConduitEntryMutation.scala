package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.world.ConduitId

case class ConduitEntryMutation(creature: CreatureId, conduit: ConduitId) extends Mutation {
  def apply(s: State): State = {
    val current = s.tmp.conduitEntry.getOrElse(List())
    val next = Some((creature, conduit) :: current)

    s.copy(tmp = s.tmp.copy(
      conduitEntry = next
    ))
  }
}
