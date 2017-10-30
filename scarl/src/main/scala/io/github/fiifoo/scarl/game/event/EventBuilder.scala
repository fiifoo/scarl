package io.github.fiifoo.scarl.game.event

import io.github.fiifoo.scarl.core.Selectors.{getItemLocation, getLocationVisibleItems}
import io.github.fiifoo.scarl.core.communication.Message
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect._

object EventBuilder {

  def apply(s: State,
            player: CreatureId,
            fov: Set[Location],
            effects: List[Effect]
           ) = {
    val builder = new EventBuilder(s, player, fov)

    effects flatMap builder.apply
  }

}

class EventBuilder(s: State, player: CreatureId, fov: Set[Location]) {

  def apply(effect: Effect): Option[Event] = {
    effect match {
      case e: BadShotEffect => build(e) map GenericEvent
      case e: CreateEntityEffect => build(e) map GenericEvent
      case e: CollideEffect => build(e) map GenericEvent
      case e: CommunicateEffect => build(e) map GenericEvent
      case e: DeathEffect => build(e) map GenericEvent
      case e: DisplaceEffect => build(e) map GenericEvent
      case e: DoorBlockedEffect => build(e) map GenericEvent
      case e: DoorUsedEffect => build(e) map GenericEvent
      case e: DropItemEffect => build(e) map GenericEvent
      case e: EquipItemEffect => build(e) map GenericEvent
      case e: ExplosionEffect => build(e)
      case e: GainLevelEffect => build(e) map GenericEvent
      case e: HealEffect => build(e) map GenericEvent
      case e: HitEffect => build(e)
      case e: MissEffect => build(e) map GenericEvent
      case e: MoveEffect => build(e)
      case e: PickItemEffect => build(e) map GenericEvent
      case e: ShootMissileEffect => build(e) map GenericEvent
      case e: ShotEffect => build(e)
      case e: TransformedEffect => build(e) map GenericEvent
      case e: TransformFailedEffect => build(e) map GenericEvent
      case e: TriggerTrapEffect => build(e) map GenericEvent
      case e: UnequipItemEffect => build(e) map GenericEvent
      case e: UseCreatureEffect => build(e) map GenericEvent
      case e: UseItemEffect => build(e) map GenericEvent
      case _ => None
    }
  }

  private def build(effect: BadShotEffect): Option[String] = {
    if (effect.attacker == player) {
      if (effect.obstacle.isDefined) {
        Some("Your shot hits obstacle.")
      } else {
        Some("You shoot at nothing.")
      }
    } else {
      None
    }
  }

  private def build(effect: CreateEntityEffect): Option[String] = {
    effect.description flatMap (description => {
      if (fov.contains(effect.location)) {
        Some(description)
      } else {
        None
      }
    })
  }

  private def build(effect: CollideEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      effect.obstacle match {
        case _: ItemId => Some("Ouch. You run straight into door.")
        case _: WallId => Some("Ouch. You run straight into wall.")
        case c: CreatureId => Some(s"${kind(c)} blocks your way.")
        case _ => None
      }
    } else {
      None
    }
  }

  private def build(effect: CommunicateEffect): Option[String] = {
    val source = effect.source
    val target = effect.target

    if (target == player) {
      val message = effect.communication map (_ (s)) collect {
        case message: Message => s"""${kind(source)} talks to you: "${message.value}""""
      } getOrElse {
        s"${kind(source)} does not respond."
      }

      Some(message)
    } else {
      None
    }
  }

  private def build(effect: DeathEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      Some("You die...")
    } else if (fov contains target(s).location) {
      Some(s"${kind(target)} is killed.")
    } else {
      None
    }
  }

  private def build(effect: DisplaceEffect): Option[String] = {
    if (effect.displacer == player) {
      Some(s"You displace ${kind(effect.displaced)}.")
    } else if (effect.displaced == player) {
      Some(s"${kind(effect.displacer)} displaces you.")
    } else {
      None
    }
  }

  private def build(effect: DoorBlockedEffect): Option[String] = {
    if (effect.user.contains(player)) {
      Some(s"${kind(effect.obstacle)} blocks the doorway.")
    } else {
      None
    }
  }

  private def build(effect: DoorUsedEffect): Option[String] = {
    if (effect.user.contains(player)) {
      if (effect.opened) {
        Some("You open the door.")
      } else {
        Some("You close the door.")
      }
    } else if (getItemLocation(s)(effect.door).exists(fov.contains)) {
      (effect.user map (user => {
        if (effect.opened) {
          Some(s"${kind(user)} opens a door.")
        } else {
          Some(s"${kind(user)} closes a door.")
        }
      })) getOrElse {
        if (effect.opened) {
          Some("Door opens.")
        } else {
          Some("Door closes.")
        }
      }
    } else {
      None
    }
  }

  private def build(effect: DropItemEffect): Option[String] = {
    val creature = effect.dropper
    val item = effect.target

    if (creature == player) {
      Some(s"You drop ${kind(item)}.")
    } else if (fov contains creature(s).location) {
      Some(s"${kind(creature)} drops ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: EquipItemEffect): Option[String] = {
    val creature = effect.creature
    val item = effect.item

    if (effect.parent.exists(_.isInstanceOf[EquipItemEffect])) {
      None
    } else if (creature == player) {
      Some(s"You equip ${kind(item)}.")
    } else if (fov contains creature(s).location) {
      Some(s"${kind(creature)} equips ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: ExplosionEffect): Option[Event] = {
    if (fov contains effect.location) {
      Some(ExplosionEvent(effect.location))
    } else {
      None
    }
  }

  private def build(effect: GainLevelEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      Some("You feel stronger.")
    } else if (fov contains target(s).location) {
      Some(s"${kind(target)} looks stronger.")
    } else {
      None
    }
  }

  private def build(effect: HealEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      Some("You feel better.")
    } else if (fov contains target(s).location) {
      Some(s"${kind(target)} looks better.")
    } else {
      None
    }
  }

  private def build(effect: HitEffect): Option[Event] = {
    val damage = effect.result.damage
    val bypass = effect.result.bypass

    val message = if (effect.attacker == player) {
      Some(if (damage.isEmpty) {
        s"You hit ${kind(effect.target)} with no effect."
      } else if (bypass.isDefined) {
        s"You hit ${kind(effect.target)} bypassing some of it's armor."
      } else {
        s"You hit ${kind(effect.target)}."
      })
    } else if (effect.target == player) {
      Some(if (damage.isEmpty) {
        s"${kind(effect.attacker)} hits you with no effect."
      } else if (bypass.isDefined) {
        s"${kind(effect.attacker)} hits you bypassing some of your armor."
      } else {
        s"${kind(effect.attacker)} hits you."
      })
    } else if (fov contains effect.target(s).location) {
      Some(if (damage.isEmpty) {
        s"${kind(effect.attacker)} hits ${kind(effect.target)} with no effect."
      } else if (bypass.isDefined) {
        s"${kind(effect.attacker)} hits ${kind(effect.target)} bypassing some of it's armor."
      } else {
        s"${kind(effect.attacker)} hits ${kind(effect.target)}."
      })
    } else {
      None
    }

    message map (HitEvent(effect.target, effect.target(s).location, _))
  }

  private def build(effect: MissEffect): Option[String] = {
    if (effect.attacker == player) {
      Some("Your attack misses.")
    } else if (effect.target == player) {
      Some(s"You evade attack.")
    } else {
      None
    }
  }

  private def build(effect: MoveEffect): Option[Event] = {
    val target = effect.target

    if (target == player) {
      val items = getLocationVisibleItems(s)(effect.to)
      val messages = items map (item => s"${kind(item)}.")

      if (messages.nonEmpty) {
        Some(GenericEvent(messages.mkString(" ")))
      } else {
        None
      }
    } else if (target(s).missile.isDefined) {
      if ((fov contains effect.from) || (fov contains effect.to)) {
        Some(MoveMissileEvent(effect.from, effect.to))
      } else {
        None
      }
    } else {
      None
    }
  }

  private def build(effect: PickItemEffect): Option[String] = {
    val creature = effect.picker
    val item = effect.target

    if (creature == player) {
      Some(s"You pick up ${kind(item)}.")
    } else if (fov contains creature(s).location) {
      Some(s"${kind(creature)} picks up ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: ShootMissileEffect): Option[String] = {
    val attacker = effect.attacker

    if (attacker == player) {
      Some("You fire missile.")
    } else if (fov contains attacker(s).location) {
      Some(s"${kind(attacker)} fires missile.")
    } else {
      None
    }
  }

  private def build(effect: ShotEffect): Option[Event] = {
    if ((fov contains effect.from) || (fov contains effect.to)) {
      Some(ShotEvent(effect.from, effect.to))
    } else {
      None
    }
  }

  private def build(effect: TransformedEffect): Option[String] = {
    effect.description flatMap (description => {
      if (fov.contains(effect.location)) {
        Some(description)
      } else {
        None
      }
    })
  }

  private def build(effect: TransformFailedEffect): Option[String] = {
    effect.description flatMap (description => {
      if (effect.owner.contains(player)) {
        Some(description)
      } else {
        None
      }
    })
  }

  private def build(effect: TriggerTrapEffect): Option[String] = {
    if (effect.triggerer == player) {
      Some(effect.description)
    } else {
      None
    }
  }

  private def build(effect: UnequipItemEffect): Option[String] = {
    val creature = effect.creature
    val item = effect.item

    if (effect.parent.exists(_.isInstanceOf[EquipItemEffect])) {
      None
    } else if (creature == player) {
      Some(s"You unequip ${kind(item)}.")
    } else if (fov contains creature(s).location) {
      Some(s"${kind(creature)} unequips ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: UseCreatureEffect): Option[String] = {
    if (effect.user == player) {
      Some(s"You use ${kind(effect.target)}.")
    } else if (fov contains effect.user(s).location) {
      Some(s"${kind(effect.user)} uses ${kind(effect.target)}.")
    } else {
      None
    }
  }

  private def build(effect: UseItemEffect): Option[String] = {
    if (effect.user == player) {
      Some(s"You use ${kind(effect.target)}.")
    } else if (fov contains effect.user(s).location) {
      Some(s"${kind(effect.user)} uses ${kind(effect.target)}.")
    } else {
      None
    }
  }

  private def kind(creature: CreatureId): String = {
    s.assets.kinds.creatures(creature(s).kind).name
  }

  private def kind(item: ItemId): String = {
    s.assets.kinds.items(item(s).kind).name
  }
}
