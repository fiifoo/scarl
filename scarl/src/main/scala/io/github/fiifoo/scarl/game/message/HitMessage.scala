package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.effect.HitEffect
import io.github.fiifoo.scarl.game.Player

object HitMessage {
  def apply(s: State, effect: HitEffect, player: Player): Option[String] = {
    val bypass = effect.result.bypass

    if (effect.attacker == player.creature) {
      Some(if (bypass.isDefined) "You hit creature bypassing some of its armor." else "You hit creature.")
    } else if (effect.target == player.creature) {
      Some(if (bypass.isDefined) "Creature hits you bypassing some of your armor." else "Creature hits you.")
    } else if (player.fov contains effect.target(s).location) {
      Some("Creature is hit.")
    } else {
      None
    }
  }
}
