package io.github.fiifoo.scarl.game.player

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{CreatureId, Selectors}
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.game.area.CreatureInfo

object PlayerInfo {
  def apply(s: State, creature: CreatureId): PlayerInfo = {
    PlayerInfo(
      CreatureInfo(s)(creature),
      Selectors.getEquipmentStats(s)(creature),
      Selectors.getCreatureKeys(s)(creature),
      s.creature.conversations.get(creature) map (_._2)
    )
  }
}

case class PlayerInfo(creature: CreatureInfo,
                      equipmentStats: Stats,
                      keys: Set[Key],
                      conversation: Option[CommunicationId],
                     )
