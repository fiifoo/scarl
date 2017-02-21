package io.github.fiifoo.scarl.game.message

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.effect._
import io.github.fiifoo.scarl.game.Player

class MessageBuilder(player: Player) {

  private var messages: List[String] = List()

  def receive(s: State, effect: Effect): Unit = {
    val message: Option[String] = effect match {
      case effect: DeathEffect => DeathMessage(s, effect, player)
      case effect: HealEffect => HealMessage(s, effect, player)
      case effect: HitEffect => HitMessage(s, effect, player)
      case effect: MissEffect => MissMessage(s, effect, player)
      case effect: TransformWidgetEffect => TransformWidgetMessage(s, effect, player)
      case _ => None
    }

    message foreach (message => messages = message :: messages)
  }

  def extract(): List[String] = {
    val m = messages
    messages = List()

    m
  }
}
