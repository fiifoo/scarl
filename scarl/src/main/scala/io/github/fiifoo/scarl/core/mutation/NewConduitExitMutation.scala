package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.world.ConduitId

case class NewConduitExitMutation(conduit: ConduitId, location: Location) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      conduits = s.conduits.copy(
        exits = s.conduits.exits + (conduit -> location)
      )
    )
  }
}
