package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.MissEffect

class MissMessage(player: () => CreatureId, fov: () => Set[Location]) {

  def apply(s: State, effect: MissEffect): Option[String] = {
    if (effect.attacker == player()) {
      Some("Your attack misses.")
    } else if (effect.target == player()) {
      Some(s"You evade attack.")
    } else {
      None
    }
  }
}
