package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.TriggerStatusId
import io.github.fiifoo.scarl.core.geometry.Location

case class TriggerLocationIndexRemoveMutation(trigger: TriggerStatusId, location: Location) {
  type Index = Map[Location, Set[TriggerStatusId]]

  def apply(index: Index): Index = {
    val triggers = index(location) - trigger

    if (triggers.isEmpty) {
      index - location
    } else {
      index + (location -> triggers)
    }
  }
}
