package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.TriggerStatusId

case class TriggerLocationIndexAddMutation(trigger: TriggerStatusId, location: Location) {
  type Index = Map[Location, List[TriggerStatusId]]

  def apply(index: Index): Index = {
    val triggers = if (index.isDefinedAt(location)) {
      trigger :: index(location)
    } else {
      List(trigger)
    }

    index + (location -> triggers)
  }
}
