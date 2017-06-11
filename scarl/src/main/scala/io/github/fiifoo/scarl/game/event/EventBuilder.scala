package io.github.fiifoo.scarl.game.event

import io.github.fiifoo.scarl.core.Selectors.{getContainerItems, getLocationEntities}
import io.github.fiifoo.scarl.core.communication.Message
import io.github.fiifoo.scarl.core.effect.{Effect, EffectListener}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId, WallId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect._

class EventBuilder(player: () => CreatureId, fov: () => Set[Location]) extends EffectListener {

  private var events: List[Event] = List()

  def apply(s: State, effect: Effect): Unit = {
    val event: Option[Event] = effect match {
      case e: BadShotEffect => build(s, e) map GenericEvent
      case e: CollideEffect => build(s, e) map GenericEvent
      case e: CommunicateEffect => build(s, e) map GenericEvent
      case e: DeathEffect => build(s, e) map GenericEvent
      case e: DoorBlockedEffect => build(s, e) map GenericEvent
      case e: DoorUsedEffect => build(s, e) map GenericEvent
      case e: EquipItemEffect => build(s, e) map GenericEvent
      case e: ExplosionEffect => build(s, e)
      case e: GainLevelEffect => build(s, e) map GenericEvent
      case e: HealEffect => build(s, e) map GenericEvent
      case e: HitEffect => build(s, e)
      case e: MissEffect => build(s, e) map GenericEvent
      case e: MoveEffect => build(s, e)
      case e: PickItemEffect => build(s, e) map GenericEvent
      case e: ShotEffect => build(s, e)
      case e: TransformedEffect => build(s, e) map GenericEvent
      case e: TransformFailedEffect => build(s, e) map GenericEvent
      case e: TriggerTrapEffect => build(s, e) map GenericEvent
      case _ => None
    }

    event foreach (event => events = event :: events)
  }

  def extract(): List[Event] = {
    val xs = events
    events = List()

    xs
  }

  private def build(s: State, effect: BadShotEffect): Option[String] = {
    if (effect.attacker == player()) {
      if (effect.obstacle.isDefined) {
        Some("Your shot hits obstacle.")
      } else {
        Some("You shoot at nothing.")
      }
    } else {
      None
    }
  }

  private def build(s: State, effect: CollideEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      effect.obstacle match {
        case _: ItemId => Some("Ouch. You run straight into door.")
        case _: WallId => Some("Ouch. You run straight into wall.")
        case c: CreatureId => Some(s"${kind(s, c)} blocks your way.")
        case _ => None
      }
    } else {
      None
    }
  }

  private def build(s: State, effect: CommunicateEffect): Option[String] = {
    val source = effect.source
    val target = effect.target

    if (target == player()) {
      val message = effect.communication map (_ (s)) collect {
        case message: Message => s"""${kind(s, source)} talks to you: "${message.value}""""
      } getOrElse {
        s"${kind(s, source)} does not respond."
      }

      Some(message)
    } else {
      None
    }
  }

  private def build(s: State, effect: DeathEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      Some("You die...")
    } else if (fov() contains target(s).location) {
      Some(s"${kind(s, target)} is killed.")
    } else {
      None
    }
  }

  private def build(s: State, effect: DoorBlockedEffect): Option[String] = {
    if (effect.opener == player()) {
      Some(s"${kind(s, effect.obstacle)} blocks the doorway.")
    } else {
      None
    }
  }

  private def build(s: State, effect: DoorUsedEffect): Option[String] = {
    if (effect.user == player()) {
      if (effect.opened) {
        Some("You open the door.")
      } else {
        Some("You close the door.")
      }
    } else {
      None
    }
  }

  private def build(s: State, effect: EquipItemEffect): Option[String] = {
    val creature = effect.creature
    val item = effect.item

    if (effect.parent.exists(_.isInstanceOf[EquipItemEffect])) {
      None
    } else if (creature == player()) {
      Some(s"You equip ${kind(s, item)}.")
    }
    else if (fov() contains creature(s).location) {
      Some(s"${kind(s, creature)} equips ${kind(s, item)}.")
    } else {
      None
    }
  }

  private def build(s: State, effect: ExplosionEffect): Option[Event] = {
    if (fov() contains effect.location) {
      Some(ExplosionEvent(effect.location))
    } else {
      None
    }
  }

  private def build(s: State, effect: GainLevelEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      Some("You feel stronger.")
    } else if (fov() contains target(s).location) {
      Some(s"${kind(s, target)} looks stronger.")
    } else {
      None
    }
  }

  private def build(s: State, effect: HealEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      Some("You feel better.")
    } else if (fov() contains target(s).location) {
      Some(s"${kind(s, target)} looks better.")
    } else {
      None
    }
  }

  private def build(s: State, effect: HitEffect): Option[Event] = {
    val bypass = effect.result.bypass

    val message = if (effect.attacker == player()) {
      Some(if (bypass.isDefined) {
        s"You hit ${kind(s, effect.target)} bypassing some of it's armor."
      } else {
        s"You hit ${kind(s, effect.target)}."
      })
    } else if (effect.target == player()) {
      Some(if (bypass.isDefined) {
        s"${kind(s, effect.attacker)} hits you bypassing some of your armor."
      } else {
        s"${kind(s, effect.attacker)} hits you."
      })
    } else if (fov() contains effect.target(s).location) {
      Some(if (bypass.isDefined) {
        s"${kind(s, effect.attacker)} hits ${kind(s, effect.target)} bypassing some of it's armor."
      } else {
        s"${kind(s, effect.attacker)} hits ${kind(s, effect.target)}."
      })
    } else {
      None
    }

    message map (HitEvent(effect.target, effect.target(s).location, _))
  }

  private def build(s: State, effect: MissEffect): Option[String] = {
    if (effect.attacker == player()) {
      Some("Your attack misses.")
    } else if (effect.target == player()) {
      Some(s"You evade attack.")
    } else {
      None
    }
  }

  private def build(s: State, effect: MoveEffect): Option[Event] = {
    val target = effect.target

    if (target == player()) {
      val entities = getLocationEntities(s)(effect.to)
      val containers = entities collect { case c: ContainerId => c }
      val items = containers flatMap getContainerItems(s)
      val messages = items map (item => s"${kind(s, item)}.")

      if (messages.nonEmpty) {
        Some(GenericEvent(messages.mkString(" ")))
      } else {
        None
      }
    } else if (target(s).missile.isDefined) {
      val f = fov()

      if ((f contains effect.from) || (f contains effect.to)) {
        Some(MoveMissileEvent(effect.from, effect.to))
      } else {
        None
      }
    } else {
      None
    }
  }

  private def build(s: State, effect: PickItemEffect): Option[String] = {
    val creature = effect.picker
    val item = effect.target

    if (creature == player()) {
      Some(s"You pick up ${kind(s, item)}.")
    } else if (fov() contains creature(s).location) {
      Some(s"${kind(s, creature)} picks up ${kind(s, item)}.")
    } else {
      None
    }
  }

  private def build(s: State, effect: ShotEffect): Option[Event] = {
    val f = fov()
    if ((f contains effect.from) || (f contains effect.to)) {
      Some(ShotEvent(effect.from, effect.to))
    } else {
      None
    }
  }

  private def build(s: State, effect: TransformedEffect): Option[String] = {
    effect.description flatMap (description => {
      if (fov().contains(effect.location)) {
        Some(description)
      } else {
        None
      }
    })
  }

  private def build(s: State, effect: TransformFailedEffect): Option[String] = {
    effect.description flatMap (description => {
      if (effect.owner.contains(player())) {
        Some(description)
      } else {
        None
      }
    })
  }

  private def build(s: State, effect: TriggerTrapEffect): Option[String] = {
    if (effect.triggerer == player()) {
      Some(effect.description)
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }

  private def kind(s: State, item: ItemId): String = {
    s.kinds.items(item(s).kind).name
  }
}
