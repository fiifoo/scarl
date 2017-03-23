package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._

case class NewConduitMutation(conduit: ConduitId, location: Location) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      conduits = s.conduits + (conduit -> location)
    )
  }
}
