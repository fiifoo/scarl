package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.LocatableId

case class LocatableLocationIndexAddMutation(locatable: LocatableId, location: Location) {
  type Index = Map[Location, List[LocatableId]]

  def apply(index: Index): Index = {
    val entities = if (index.isDefinedAt(location)) {
      locatable :: index(location)
    } else {
      List(locatable)
    }

    index + (location -> entities)
  }
}
