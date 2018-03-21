package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.geometry.Sector

case class ItemSectorIndexAddMutation(item: ItemId, sector: Sector) {
  type Index = Map[Sector, Set[ItemId]]

  def apply(index: Index): Index = {
    val items = if (index.isDefinedAt(sector)) {
      index(sector) + item
    } else {
      Set(item)
    }

    index + (sector -> items)
  }
}
