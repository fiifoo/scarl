package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, ItemId}

case class ItemContainerIndexAddMutation(item: ItemId, container: EntityId) {
  type Index = Map[EntityId, List[ItemId]]

  def apply(index: Index): Index = {
    val items = if (index.isDefinedAt(container)) {
      item :: index(container)
    } else {
      List(item)
    }

    index + (container -> items)
  }
}
