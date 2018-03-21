package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Sector
import io.github.fiifoo.scarl.core.mutation.index._

case class ItemContainerMutation(item: ItemId, container: EntityId) extends Mutation {

  def apply(s: State): State = {
    val previous = item(s)
    val next = previous.copy(container = container)

    s.copy(
      entities = s.entities + (item -> next),
      index = mutateIndex(s, previous, next)
    )
  }

  private def mutateIndex(s: State, previous: Item, next: Item): State.Index = {
    s.index.copy(
      containerItems = mutateContainerItemsIndex(s, previous, next),
      sectorItems = mutateSectorItemsIndex(s, previous, next)
    )
  }

  private def mutateContainerItemsIndex(s: State,
                                        previous: Item,
                                        next: Item,
                                       ): Map[EntityId, Set[ItemId]] = {
    val remove = ItemContainerIndexRemoveMutation(item, previous.container)
    val add = ItemContainerIndexAddMutation(item, next.container)

    add(remove(s.index.containerItems))
  }

  private def mutateSectorItemsIndex(s: State,
                                     previous: Item,
                                     next: Item,
                                    ): Map[Sector, Set[ItemId]] = {
    var index = s.index.sectorItems
    index = previous.container match {
      case container: ContainerId => ItemSectorIndexRemoveMutation(item, Sector(s)(container(s).location))(index)
      case _ => index
    }
    index = next.container match {
      case container: ContainerId => ItemSectorIndexAddMutation(item, Sector(s)(container(s).location))(index)
      case _ => index
    }

    index
  }
}
