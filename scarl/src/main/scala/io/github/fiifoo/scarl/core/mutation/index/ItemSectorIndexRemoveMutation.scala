package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.geometry.Sector

case class ItemSectorIndexRemoveMutation(item: ItemId, sector: Sector) {
  type Index = Map[Sector, Set[ItemId]]

  def apply(index: Index): Index = {
    val items = index(sector) - item

    if (items.isEmpty) {
      index - sector
    } else {
      index + (sector -> items)
    }
  }
}
