package fi.fiifoo.scarl.core.mutation.index

import fi.fiifoo.scarl.core.Location
import fi.fiifoo.scarl.core.entity.LocatableId

case class LocatableLocationIndexAddMutation(locatable: LocatableId, location: Location) {

  def apply(s: Map[Location, List[LocatableId]]): Map[Location, List[LocatableId]] = {
    val entities = if (s.isDefinedAt(location)) {
      locatable :: s(location)
    } else {
      List(locatable)
    }

    s + (location -> entities)
  }
}
