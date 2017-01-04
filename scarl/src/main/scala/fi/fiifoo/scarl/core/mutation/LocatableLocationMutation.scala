package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.entity.{Locatable, LocatableId}
import fi.fiifoo.scarl.core.mutation.index.{LocatableLocationIndexAddMutation, LocatableLocationIndexRemoveMutation}
import fi.fiifoo.scarl.core.{Location, State, StateIndex}

case class LocatableLocationMutation(locatable: LocatableId, location: Location) extends Mutation {

  def apply(s: State): State = {
    val previous = locatable(s)
    val next = previous.setLocation(location)

    s.copy(
      entities = s.entities + (locatable -> next),
      index = mutateIndex(s.index, previous, next)
    )
  }

  private def mutateIndex(index: StateIndex, previous: Locatable, next: Locatable): StateIndex = {
    val remove = LocatableLocationIndexRemoveMutation(locatable, previous.location)
    val add = LocatableLocationIndexAddMutation(locatable, next.location)

    index.copy(
      entities = index.entities.copy(
        location = add(remove(index.entities.location))
      )
    )
  }

}
