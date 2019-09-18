package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.mutation.cache.EquipmentStatsCacheMutation

case class EquipItemMutation(creature: CreatureId,
                             item: ItemId,
                             slots: Set[Slot]) extends Mutation {

  def apply(s: State): State = {
    val prev = s.creature.equipments.getOrElse(creature, Map())
    val add = (slots map ((_, item))).toMap
    val next = prev ++ add

    val s1 = s.copy(creature = s.creature.copy(
      equipments = s.creature.equipments + (creature -> next)
    ))

    s1.copy(cache = s1.cache.copy(
      equipmentStats = EquipmentStatsCacheMutation(creature)(s1, s1.cache.equipmentStats)
    ))
  }
}
