package fi.fiifoo.scarl.core.mutation.index

import fi.fiifoo.scarl.core.entity.{EntityId, ItemId}

case class ItemContainerIndexRemoveMutation(item: ItemId, container: EntityId) {

  def apply(s: Map[EntityId, List[ItemId]]): Map[EntityId, List[ItemId]] = {
    val items = s(container) filter (_ != item)

    if (items.isEmpty) {
      s - container
    } else {
      s + (container -> items)
    }
  }
}
