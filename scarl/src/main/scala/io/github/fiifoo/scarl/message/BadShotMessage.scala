package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.BadShotEffect

class BadShotMessage(player: () => CreatureId,
                     fov: () => Set[Location]
                    ) extends MessageBuilder[BadShotEffect] {

  def apply(s: State, effect: BadShotEffect): Option[String] = {
    if (effect.attacker == player()) {
      if (effect.wall.isDefined) {
        Some("Your shot hits wall.")
      } else {
        Some("You shoot at nothing.")
      }
    } else {
      None
    }
  }
}
