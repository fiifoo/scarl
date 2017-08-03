package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.{Selectors, State}

object PlayerInfo {
  def apply(s: State, creature: CreatureId): PlayerInfo = {
    PlayerInfo(creature(s), Selectors.getEquipmentStats(s)(creature))
  }
}

case class PlayerInfo(creature: Creature, equipmentStats: Stats)
