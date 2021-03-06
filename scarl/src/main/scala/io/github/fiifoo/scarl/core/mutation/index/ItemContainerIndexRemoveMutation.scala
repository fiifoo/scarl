package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, ItemId}

case class ItemContainerIndexRemoveMutation(item: ItemId, container: EntityId) {
  type Index = Map[EntityId, Set[ItemId]]

  def apply(index: Index): Index = {
    val items = index(container) - item

    if (items.isEmpty) {
      index - container
    } else {
      index + (container -> items)
    }
  }
}
