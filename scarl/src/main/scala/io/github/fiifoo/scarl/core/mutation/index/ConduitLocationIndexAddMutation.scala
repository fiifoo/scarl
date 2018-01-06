package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.world.ConduitId

case class ConduitLocationIndexAddMutation(conduit: ConduitId, location: Location) {
  type Index = Map[Location, ConduitId]

  def apply(index: Index): Index = {
    index + (location -> conduit)
  }
}
