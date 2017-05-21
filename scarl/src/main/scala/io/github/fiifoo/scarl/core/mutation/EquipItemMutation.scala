package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.mutation.index.EquipmentStatsIndexMutation

case class EquipItemMutation(creature: CreatureId,
                             item: ItemId,
                             slots: Set[Slot]) extends Mutation {

  def apply(s: State): State = {
    val prev = s.equipments.getOrElse(creature, Map())
    val add = (slots map ((_, item))).toMap
    val next = prev ++ add

    val s1 = s.copy(equipments = s.equipments + (creature -> next))

    s1.copy(index = s1.index.copy(
      equipmentStats = EquipmentStatsIndexMutation(creature)(s1, s1.index.equipmentStats)
    ))
  }
}
