package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.effect.DeathEffect
import io.github.fiifoo.scarl.game.Player

object DeathMessage {
  def apply(s: State, effect: DeathEffect, player: Player): Option[String] = {
    if (effect.target == player.creature) {
      Some("You die...")
    } else if (player.fov contains effect.target(s).location) {
      Some("Creature is killed.")
    } else {
      None
    }
  }
}
