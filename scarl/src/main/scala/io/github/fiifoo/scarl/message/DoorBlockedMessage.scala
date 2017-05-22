package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.DoorBlockedEffect

class DoorBlockedMessage(player: () => CreatureId,
                         fov: () => Set[Location]
                        ) extends MessageBuilder[DoorBlockedEffect] {

  def apply(s: State, effect: DoorBlockedEffect): Option[String] = {
    if (effect.opener == player()) {
      Some(s"${kind(s, effect.obstacle)} blocks the doorway.")
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
