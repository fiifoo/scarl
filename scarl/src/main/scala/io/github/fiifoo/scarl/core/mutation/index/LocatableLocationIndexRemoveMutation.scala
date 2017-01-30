package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.LocatableId

case class LocatableLocationIndexRemoveMutation(locatable: LocatableId, location: Location) {
  type Index = Map[Location, List[LocatableId]]

  def apply(index: Index): Index = {
    val entities = index(location) filter (_ != locatable)

    if (entities.isEmpty) {
      index - location
    } else {
      index + (location -> entities)
    }
  }
}
