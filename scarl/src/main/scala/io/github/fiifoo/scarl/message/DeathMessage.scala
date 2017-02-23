package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.DeathEffect
import io.github.fiifoo.scarl.game.Player

object DeathMessage {

  def apply(s: State, effect: DeathEffect, player: Player): Option[String] = {
    val target = effect.target

    if (target == player.creature) {
      Some("You die...")
    } else if (player.fov contains target(s).location) {
      Some(s"${kind(s, target)} is killed.")
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
