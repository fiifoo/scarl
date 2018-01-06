package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.TriggerStatusId
import io.github.fiifoo.scarl.core.geometry.Location

case class TriggerLocationIndexAddMutation(trigger: TriggerStatusId, location: Location) {
  type Index = Map[Location, Set[TriggerStatusId]]

  def apply(index: Index): Index = {
    val triggers = if (index.isDefinedAt(location)) {
      index(location) + trigger
    } else {
      Set(trigger)
    }

    index + (location -> triggers)
  }
}
