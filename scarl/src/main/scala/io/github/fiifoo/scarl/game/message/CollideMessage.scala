package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, WallId}
import io.github.fiifoo.scarl.effect.{CollideEffect, DeathEffect}
import io.github.fiifoo.scarl.game.Player

object CollideMessage {

  def apply(s: State, effect: CollideEffect, player: Player): Option[String] = {
    val target = effect.target

    if (target == player.creature) {
      effect.obstacle match {
        case w: WallId => Some("Ouch. You run straight into wall.")
        case c: CreatureId => Some(s"${kind(s, c)} blocks your way.")
        case _ => None
      }
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
