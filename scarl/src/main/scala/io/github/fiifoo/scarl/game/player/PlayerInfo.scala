package io.github.fiifoo.scarl.game.player

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, Selectors}
import io.github.fiifoo.scarl.core.item.Key

object PlayerInfo {
  def apply(s: State, creature: CreatureId): PlayerInfo = {
    PlayerInfo(
      creature(s),
      Selectors.getEquipmentStats(s)(creature),
      s.keys.getOrElse(creature, Set())
    )
  }
}

case class PlayerInfo(creature: Creature, equipmentStats: Stats, keys: Set[Key])
