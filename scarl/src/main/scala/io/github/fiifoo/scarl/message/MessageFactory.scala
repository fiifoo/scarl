package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.effect.{Effect, EffectListener}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect._

class MessageFactory(player: () => CreatureId, fov: () => Set[Location]) extends EffectListener {

  private var messages: List[String] = List()

  implicit val badShot = new BadShotMessage(player, fov)
  implicit val collide = new CollideMessage(player, fov)
  implicit val communicate = new CommunicateMessage(player, fov)
  implicit val death = new DeathMessage(player, fov)
  implicit val doorBlocked = new DoorBlockedMessage(player, fov)
  implicit val doorUsed = new DoorUsedMessage(player, fov)
  implicit val equipItem = new EquipItemMessage(player, fov)
  implicit val gainLevel = new GainLevelMessage(player, fov)
  implicit val heal = new HealMessage(player, fov)
  implicit val hit = new HitMessage(player, fov)
  implicit val miss = new MissMessage(player, fov)
  implicit val move = new MoveMessage(player, fov)
  implicit val pickItem = new PickItemMessage(player, fov)
  implicit val transformWidget = new TransformWidgetMessage(player, fov)

  def apply(s: State, effect: Effect): Unit = {
    val message: Option[String] = effect match {
      case e: BadShotEffect => build(s, e)
      case e: CollideEffect => build(s, e)
      case e: CommunicateEffect => build(s, e)
      case e: DeathEffect => build(s, e)
      case e: DoorBlockedEffect => build(s, e)
      case e: DoorUsedEffect => build(s, e)
      case e: EquipItemEffect => build(s, e)
      case e: GainLevelEffect => build(s, e)
      case e: HealEffect => build(s, e)
      case e: HitEffect => build(s, e)
      case e: MissEffect => build(s, e)
      case e: MoveEffect => build(s, e)
      case e: PickItemEffect => build(s, e)
      case e: TransformWidgetEffect => build(s, e)
      case _ => None
    }

    message foreach (message => messages = message :: messages)
  }

  def extract(): List[String] = {
    val m = messages
    messages = List()

    m
  }

  private def build[E <: Effect](s: State, effect: E)(implicit builder: MessageBuilder[E]): Option[String] = {
    builder(s, effect)
  }
}
