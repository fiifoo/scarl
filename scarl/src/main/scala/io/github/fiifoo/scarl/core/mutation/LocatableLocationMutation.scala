package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, Locatable, LocatableId}
import io.github.fiifoo.scarl.core.geometry.{Location, Sector}
import io.github.fiifoo.scarl.core.mutation.index.{CreatureSectorIndexAddMutation, CreatureSectorIndexRemoveMutation, LocatableLocationIndexAddMutation, LocatableLocationIndexRemoveMutation}

case class LocatableLocationMutation(locatable: LocatableId, location: Location) extends Mutation {

  def apply(s: State): State = {
    val previous = locatable(s)
    val next = previous.setLocation(location)

    s.copy(
      entities = s.entities + (locatable -> next),
      index = mutateIndex(s, previous, next)
    )
  }

  private def mutateIndex(s: State, previous: Locatable, next: Locatable): State.Index = {
    locatable match {
      case _: ContainerId => throw new Exception("Mutating location trigger index not implemented. Moving container not allowed without it.")
      case _ =>
    }

    s.index.copy(
      locationEntities = mutateLocationEntitiesIndex(s, previous, next),
      sectorCreatures = locatable match {
        case creature: CreatureId => mutateSectorCreaturesIndex(s, creature, previous, next)
        case _ => s.index.sectorCreatures
      }
    )
  }

  private def mutateLocationEntitiesIndex(s: State,
                                          previous: Locatable,
                                          next: Locatable,
                                         ): Map[Location, Set[LocatableId]] = {
    val remove = LocatableLocationIndexRemoveMutation(locatable, previous.location)
    val add = LocatableLocationIndexAddMutation(locatable, next.location)

    add(remove(s.index.locationEntities))
  }

  private def mutateSectorCreaturesIndex(s: State,
                                         creature: CreatureId,
                                         previous: Locatable,
                                         next: Locatable,
                                        ): Map[Sector, Set[CreatureId]] = {
    val previousSector = Sector(s)(previous.location)
    val nextSector = Sector(s)(next.location)

    if (previousSector != nextSector) {
      val remove = CreatureSectorIndexRemoveMutation(creature, previousSector)
      val add = CreatureSectorIndexAddMutation(creature, nextSector)

      add(remove(s.index.sectorCreatures))
    } else {
      s.index.sectorCreatures
    }
  }
}
