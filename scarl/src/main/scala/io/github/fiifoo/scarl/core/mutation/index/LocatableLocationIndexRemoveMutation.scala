package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.LocatableId

case class LocatableLocationIndexRemoveMutation(locatable: LocatableId, location: Location) {

  def apply(s: Map[Location, List[LocatableId]]): Map[Location, List[LocatableId]] = {
    val entities = s(location) filter (_ != locatable)

    if (entities.isEmpty) {
      s - location
    } else {
      s + (location -> entities)
    }
  }
}
