package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.HealEffect
import io.github.fiifoo.scarl.game.Player

object HealMessage {

  def apply(s: State, effect: HealEffect, player: Player): Option[String] = {
    val target = effect.target

    if (target == player.creature) {
      Some("You feel better.")
    } else if (player.fov contains target(s).location) {
      Some(s"${kind(s, target)} looks better.")
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
