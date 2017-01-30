package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, ItemId}

case class ItemContainerIndexRemoveMutation(item: ItemId, container: EntityId) {
  type Index = Map[EntityId, List[ItemId]]

  def apply(index: Index): Index = {
    val items = index(container) filter (_ != item)

    if (items.isEmpty) {
      index - container
    } else {
      index + (container -> items)
    }
  }
}
