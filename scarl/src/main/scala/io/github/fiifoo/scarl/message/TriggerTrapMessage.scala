package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.TriggerTrapEffect

class TriggerTrapMessage(player: () => CreatureId,
                         fov: () => Set[Location]
                        ) extends MessageBuilder[TriggerTrapEffect] {

  def apply(s: State, effect: TriggerTrapEffect): Option[String] = {
    if (effect.triggerer == player()) {
      Some(effect.description)
    } else {
      None
    }
  }
}
