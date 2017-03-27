package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.LocatableId

case class LocatableLocationIndexAddMutation(locatable: LocatableId, location: Location) {
  type Index = Map[Location, Set[LocatableId]]

  def apply(index: Index): Index = {
    val entities = if (index.isDefinedAt(location)) {
      index(location) + locatable
    } else {
      Set(locatable)
    }

    index + (location -> entities)
  }
}
