package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.MachineryId

case class LocationMachineryIndexRemoveMutation(machinery: MachineryId, locations: Set[Location]) {
  type Index = Map[Location, Set[MachineryId]]

  def apply(index: Index): Index = {
    (locations foldLeft index) ((index, location) => {
      val next = index(location) - machinery
      if (next.isEmpty) {
        index - location
      } else {
        index + (location -> next)
      }
    })
  }
}
