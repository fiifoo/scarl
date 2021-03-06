package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.MachineryId
import io.github.fiifoo.scarl.core.geometry.Location

case class LocationMachineryIndexAddMutation(machinery: MachineryId, locations: Set[Location]) {
  type Index = Map[Location, Set[MachineryId]]

  def apply(index: Index): Index = {
    index ++ (locations map (location => {
      if (index.isDefinedAt(location)) {
        location -> (index(location) + machinery)
      } else {
        location -> Set(machinery)
      }
    })).toMap
  }
}
