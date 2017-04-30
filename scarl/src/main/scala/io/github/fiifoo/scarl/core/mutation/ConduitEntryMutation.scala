package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.world.ConduitId

case class ConduitEntryMutation(creature: CreatureId, conduit: ConduitId) extends Mutation {
  def apply(s: State): State = {
    if (s.tmp.conduitEntry.isDefined) {
      throw new Exception("Conduit entry exists. Only one allowed.")
    }

    s.copy(tmp = s.tmp.copy(
      conduitEntry = Some((conduit, creature(s)))
    ))
  }
}
