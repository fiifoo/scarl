package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.HitEffect
import io.github.fiifoo.scarl.game.Player

object HitMessage {

  def apply(s: State, effect: HitEffect, player: Player): Option[String] = {
    val bypass = effect.result.bypass

    if (effect.attacker == player.creature) {
      Some(if (bypass.isDefined) {
        s"You hit ${kind(s, effect.target)} bypassing some of it's armor."
      } else {
        s"You hit ${kind(s, effect.target)}."
      })
    } else if (effect.target == player.creature) {
      Some(if (bypass.isDefined) {
        s"${kind(s, effect.attacker)} hits you bypassing some of your armor."
      } else {
        s"${kind(s, effect.attacker)} hits you."
      })
    } else if (player.fov contains effect.target(s).location) {
      Some(if (bypass.isDefined) {
        s"${kind(s, effect.attacker)} hits ${kind(s, effect.target)} bypassing some of it's armor."
      } else {
        s"${kind(s, effect.attacker)} hits ${kind(s, effect.target)}."
      })
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }

}
