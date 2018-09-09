package io.github.fiifoo.scarl.game.player

import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Settings(equipmentSet: Int = 1,
                    equipmentSets: Map[Int, Map[Slot, ItemId]] = Map(),
                    quickItems: Map[Int, ItemKindId] = Map()
                   ) {

  def changeEquipmentSet(newSet: Int, currentEquipments: Map[Slot, ItemId]): Settings = {
    this.copy(
      equipmentSet = newSet,
      equipmentSets = this.equipmentSets + (this.equipmentSet -> currentEquipments)
    )
  }

  def setQuickItem(slot: Int, item: Option[ItemKindId]): Settings = {
    val next = item map (item => {
      (this.quickItems filter (_._2 != item)) + (slot -> item)
    }) getOrElse {
      this.quickItems - slot
    }

    this.copy(quickItems = next)
  }
}
