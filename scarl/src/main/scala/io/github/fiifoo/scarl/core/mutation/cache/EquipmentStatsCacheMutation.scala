package io.github.fiifoo.scarl.core.mutation.cache

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.rule.EquipmentStatsRule

case class EquipmentStatsCacheMutation(creature: CreatureId) {
  type Cache = Map[CreatureId, Stats]

  def apply(s: State, cache: Cache): Cache = {
    val equipments = s.creature.equipments.getOrElse(creature, Map())
    val stats = EquipmentStatsRule(s, equipments)

    cache + (creature -> stats)
  }
}
