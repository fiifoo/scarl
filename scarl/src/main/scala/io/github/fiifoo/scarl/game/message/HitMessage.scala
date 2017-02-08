package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.effect.HitEffect
import io.github.fiifoo.scarl.game.Player

object HitMessage {
  def apply(s: State, effect: HitEffect, player: Player): Option[String] = {
    if (effect.attacker == player.creature) {
      Some("You hit creature.")
    } else if (effect.target == player.creature) {
      Some("Creature hits you.")
    } else if (player.fov contains effect.target(s).location) {
      Some("Creature is hit.")
    } else {
      None
    }
  }
}
