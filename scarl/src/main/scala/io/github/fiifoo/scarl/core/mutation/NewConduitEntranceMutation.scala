package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.index.ConduitLocationIndexAddMutation
import io.github.fiifoo.scarl.core.world.ConduitId

case class NewConduitEntranceMutation(conduit: ConduitId, location: Location) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      conduits = s.conduits.copy(
        entrances = s.conduits.entrances + (conduit -> location)
      ),
      index = s.index.copy(
        locationConduit = ConduitLocationIndexAddMutation(conduit, location)(s.index.locationConduit)
      )
    )
  }
}
