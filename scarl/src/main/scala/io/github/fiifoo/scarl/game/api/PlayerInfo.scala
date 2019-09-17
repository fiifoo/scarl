package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{CreatureId, Selectors}
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.game.RunState

object PlayerInfo {
  def apply(state: RunState): PlayerInfo = {
    val previous = state.previous map (PlayerInfo(_))

    create(state.instance, state.game.player, previous)
  }

  private def create(s: State, creature: CreatureId, previous: Option[PlayerInfo]): PlayerInfo = {
    val equipmentStats = Selectors.getEquipmentStats(s)(creature)
    val keys = Selectors.getCreatureKeys(s)(creature)

    PlayerInfo(
      creature = CreatureInfo(s)(creature),
      equipmentStats = if (previous.exists(_.equipmentStats.contains(equipmentStats))) None else Some(equipmentStats),
      keys = if (previous.exists(_.keys.contains(keys))) None else Some(keys),
      conversation = s.creature.conversations.get(creature) map (_._2)
    )
  }
}

case class PlayerInfo(creature: CreatureInfo,
                      equipmentStats: Option[Stats],
                      keys: Option[Set[Key]],
                      conversation: Option[CommunicationId],
                     )
