package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.DoorUsedEffect

class DoorUsedMessage(player: () => CreatureId,
                      fov: () => Set[Location]
                     ) extends MessageBuilder[DoorUsedEffect] {

  def apply(s: State, effect: DoorUsedEffect): Option[String] = {
    if (effect.user == player()) {
      if (effect.opened) {
        Some("You open the door.")
      } else {
        Some("You close the door.")
      }
    } else {
      None
    }
  }
}
