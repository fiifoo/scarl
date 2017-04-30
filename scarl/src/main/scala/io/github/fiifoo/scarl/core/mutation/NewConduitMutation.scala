package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.world.ConduitId

case class NewConduitMutation(conduit: ConduitId, location: Location) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      conduits = s.conduits + (conduit -> location)
    )
  }
}
