package fi.fiifoo.scarl.core.mutation.index

import fi.fiifoo.scarl.core.Location
import fi.fiifoo.scarl.core.entity.LocatableId

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
