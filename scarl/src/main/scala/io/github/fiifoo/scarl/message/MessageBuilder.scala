package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.effect.{Effect, EffectListener}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect._

class MessageBuilder(player: () => CreatureId, fov: () => Set[Location]) extends EffectListener {

  private var messages: List[String] = List()

  val collide = new CollideMessage(player, fov)
  val death = new DeathMessage(player, fov)
  val heal = new HealMessage(player, fov)
  val hit = new HitMessage(player, fov)
  val miss = new MissMessage(player, fov)
  val transformWidget = new TransformWidgetMessage(player, fov)

  def apply(s: State, effect: Effect): Unit = {
    val message: Option[String] = effect match {
      case effect: CollideEffect => collide(s, effect)
      case effect: DeathEffect => death(s, effect)
      case effect: HealEffect => heal(s, effect)
      case effect: HitEffect => hit(s, effect)
      case effect: MissEffect => miss(s, effect)
      case effect: TransformWidgetEffect => transformWidget(s, effect)
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
