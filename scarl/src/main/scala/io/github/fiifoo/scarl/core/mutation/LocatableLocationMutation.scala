package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{ContainerId, Locatable, LocatableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.index.{LocatableLocationIndexAddMutation, LocatableLocationIndexRemoveMutation}

case class LocatableLocationMutation(locatable: LocatableId, location: Location) extends Mutation {

  def apply(s: State): State = {
    val previous = locatable(s)
    val next = previous.setLocation(location)

    s.copy(
      entities = s.entities + (locatable -> next),
      index = mutateIndex(s.index, previous, next)
    )
  }

  private def mutateIndex(index: State.Index, previous: Locatable, next: Locatable): State.Index = {
    val remove = LocatableLocationIndexRemoveMutation(locatable, previous.location)
    val add = LocatableLocationIndexAddMutation(locatable, next.location)

    locatable match {
      case _: ContainerId => throw new Exception("Mutating location trigger index not implemented. Moving container not allowed without it.")
      case _ =>
    }

    index.copy(locationEntities = add(remove(index.locationEntities)))
  }

}
