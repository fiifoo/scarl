package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.entity.{EntityId, Item, ItemId}
import io.github.fiifoo.scarl.core.mutation.index.{ItemContainerIndexAddMutation, ItemContainerIndexRemoveMutation}
import io.github.fiifoo.scarl.core.{State, StateIndex}

case class ItemContainerMutation(item: ItemId, container: EntityId) extends Mutation {

  def apply(s: State): State = {
    val previous = item(s)
    val next = previous.copy(container = container)

    s.copy(
      entities = s.entities + (item -> next),
      index = mutateIndex(s.index, previous, next)
    )
  }

  private def mutateIndex(index: StateIndex, previous: Item, next: Item): StateIndex = {
    val remove = ItemContainerIndexRemoveMutation(item, previous.container)
    val add = ItemContainerIndexAddMutation(item, next.container)

    index.copy(
      items = index.items.copy(
        container = add(remove(index.items.container))
      )
    )
  }
}
