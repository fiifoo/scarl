package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.geometry.Location

case class LocatableLocationIndexRemoveMutation(locatable: LocatableId, location: Location) {
  type Index = Map[Location, Set[LocatableId]]

  def apply(index: Index): Index = {
    val entities = index(location) - locatable

    if (entities.isEmpty) {
      index - location
    } else {
      index + (location -> entities)
    }
  }
}
