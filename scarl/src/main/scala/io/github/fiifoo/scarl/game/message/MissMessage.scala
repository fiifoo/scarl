package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.effect.MissEffect
import io.github.fiifoo.scarl.game.Player

object MissMessage {
  def apply(s: State, effect: MissEffect, player: Player): Option[String] = {
    if (effect.attacker == player.creature) {
      Some("Your attack misses.")
    } else if (effect.target == player.creature) {
      Some("You evade creatures attack.")
    } else {
      None
    }
  }
}
