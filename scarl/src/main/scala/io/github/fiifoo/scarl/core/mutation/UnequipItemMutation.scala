package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.index.EquipmentStatsIndexMutation

case class UnequipItemMutation(creature: CreatureId, item: ItemId) extends Mutation {

  def apply(s: State): State = {
    val prev = s.equipments.getOrElse(creature, Map())
    val remove = prev filter (_._2 == item)
    val next = prev -- remove.keys

    val s1 = s.copy(equipments = s.equipments + (creature -> next))

    s1.copy(index = s1.index.copy(
      equipmentStats = EquipmentStatsIndexMutation(creature)(s, s.index.equipmentStats)
    ))
  }
}
