package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{EntityId, ItemId}

case class ItemContainerIndexAddMutation(item: ItemId, container: EntityId) {

  def apply(s: Map[EntityId, List[ItemId]]): Map[EntityId, List[ItemId]] = {
    val items = if (s.isDefinedAt(container)) {
      item :: s(container)
    } else {
      List(item)
    }

    s + (container -> items)
  }
}
