package io.github.fiifoo.scarl.game.event

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.{CreateEntityEffect, Effect, LocalizedDescriptionEffect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.Selectors.{getContainerItems, getCreatureStats, getLocationVisibleItems}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.{KeyKindId, SharedKey}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.effect.area.{ExplosiveTimerEffect, SignaledEffect, TransformBlockedEffect}
import io.github.fiifoo.scarl.effect.combat._
import io.github.fiifoo.scarl.effect.creature._
import io.github.fiifoo.scarl.effect.creature.condition._
import io.github.fiifoo.scarl.effect.interact._
import io.github.fiifoo.scarl.effect.movement.{CollideEffect, DisplacedEffect, MovedEffect}
import io.github.fiifoo.scarl.rule.{HackRule, SignalRule}

object EventBuilder {

  val PARAM_TARGET = "target"

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

  lazy private val calculateSignal = {
    val from = player(s).location
    val sensors = getCreatureStats(s)(player).sight.sensors

    SignalRule.calculateSignal(from, sensors) _
  }

  def apply(effect: Effect): Option[Event] = {
    effect match {
      case e: BadShotEffect => build(e) map GenericEvent
      case e: BlockedDoorEffect => build(e) map GenericEvent
      case e: CancelRecycleItemEffect => build(e) map GenericEvent
      case e: CaptureEffect => build(e) map GenericEvent
      case e: CollideEffect => build(e) map GenericEvent
      case e: CommunicateEffect => build(e) map GenericEvent
      case e: CreateEntityEffect => build(e) map GenericEvent
      case e: DeathEffect => build(e) map GenericEvent
      case e: DetectEffect => build(e) map GenericEvent
      case e: DisplacedEffect => build(e) map GenericEvent
      case e: DoorUsedEffect => build(e) map GenericEvent
      case e: DropItemEffect => build(e) map GenericEvent
      case e: EquipItemEffect => build(e) map GenericEvent
      case e: EquipWeaponsEffect => build(e) map GenericEvent
      case e: ExplodeEffect => build(e) map GenericEvent
      case e: ExplosionHitEffect => build(e)
      case e: ExplosionLocationEffect => build(e)
      case e: ExplosionMissEffect => build(e) map GenericEvent
      case e: ExplosiveTimerEffect => build(e) map GenericEvent
      case e: GainConditionEffect => build(e) map GenericEvent
      case e: GainLevelEffect => build(e) map GenericEvent
      case e: HealEffect => build(e) map GenericEvent
      case e: HitEffect => build(e)
      case e: HackedEffect => build(e) map GenericEvent
      case e: HackFailedEffect => build(e) map GenericEvent
      case e: ItemCraftedEffect => build(e) map GenericEvent
      case e: LocalizedDescriptionEffect => build(e) map GenericEvent
      case e: LockedUsableEffect => build(e) map GenericEvent
      case e: LoseConditionEffect => build(e) map GenericEvent
      case e: MachineryActivatedEffect => build(e) map GenericEvent
      case e: MissEffect => build(e) map GenericEvent
      case e: MovedEffect => build(e)
      case e: NoResponseEffect => build(e) map GenericEvent
      case e: PickItemEffect => build(e) map GenericEvent
      case e: PowerUsedEffect => build(e) map GenericEvent
      case e: ReceiveCommunicationEffect => build(e)
      case e: ReceiveKeyEffect => build(e) map GenericEvent
      case e: RecycleItemEffect => build(e) map GenericEvent
      case e: RemoveEntityEffect => build(e) map GenericEvent
      case e: ResistConditionEffect => build(e) map GenericEvent
      case e: SignaledEffect => build(e)
      case e: ShootMissileEffect => build(e) map GenericEvent
      case e: ShortageEffect => build(e) map GenericEvent
      case e: ShotEffect => build(e)
      case e: TransformBlockedEffect => build(e) map GenericEvent
      case e: TrapHitEffect => build(e)
      case e: TrapMissEffect => build(e) map GenericEvent
      case e: UnequipItemEffect => build(e) map GenericEvent
      case e: UseUsableEffect => build(e) map GenericEvent
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

  private def build(effect: BlockedDoorEffect): Option[String] = {
    if (effect.user contains player) {
      Some(s"${kind(effect.obstacle)} is in way.")
    } else {
      None
    }
  }

  private def build(effect: CancelRecycleItemEffect): Option[String] = {
    if (effect.recycler == player) {
      Some(s"You cancel recycling of ${kind(effect.item)}.")
    } else {
      None
    }
  }

  private def build(effect: CaptureEffect): Option[String] = {
    if (effect.capturer == player || fov.contains(effect.location)) {
      Some(s"${kind(effect.target)} is captured.")
    } else {
      None
    }
  }

  private def build(effect: CollideEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      effect.obstacle match {
        case _: ItemId => Some("Ouch. You run straight into door.")
        case _: WallId => Some("Ouch. You run straight into wall.")
        case c: CreatureId => Some(s"${kind(c)} blocks your way.")
        case t: TerrainId => Some(s"${kind(t)} is impassable.")
        case _ => None
      }
    } else {
      None
    }
  }

  private def build(effect: CommunicateEffect): Option[String] = {
    if (effect.source == player) {
      Some(s"You talk to ${kind(effect.target)}.")
    } else {
      None
    }
  }

  private def build(effect: CreateEntityEffect): Option[String] = {
    effect.description flatMap (description => {
      if (fov contains effect.location) {
        val target = kind(effect.kind)
        val result = target map parse(description, EventBuilder.PARAM_TARGET) getOrElse description

        Some(result)
      } else {
        None
      }
    })
  }

  private def build(effect: DeathEffect): Option[String] = {
    val target = effect.target

    if (hasParentEffect(effect, {
      case _: BurnEffect => true
      case _ => false
    })) {
      if (target == player) {
        Some("You burn to death...")
      } else if (fov contains effect.location) {
        Some(s"${kind(target)} is burned to death.")
      } else {
        None
      }
    } else {
      if (target == player) {
        Some("You die...")
      } else if (fov contains effect.location) {
        Some(s"${kind(target)} is killed.")
      } else {
        None
      }
    }
  }

  private def build(effect: DetectEffect): Option[String] = {
    if (effect.creature == player) {
      Some(s"You detect ${kind(effect.item)}.")
    } else {
      None
    }
  }

  private def build(effect: DisplacedEffect): Option[String] = {
    if (effect.displacer == player) {
      Some(s"You displace ${kind(effect.displaced)}.")
    } else if (effect.displaced == player) {
      Some(s"${kind(effect.displacer)} displaces you.")
    } else {
      None
    }
  }

  private def build(effect: DoorUsedEffect): Option[String] = {
    if (effect.user contains player) {
      if (effect.opened) {
        Some(s"You open ${kind(effect.door)}.")
      } else {
        Some(s"You close ${kind(effect.door)}.")
      }
    } else if (fov contains effect.location) {
      (effect.user map (user => {
        if (effect.opened) {
          Some(s"${kind(user)} opens ${kind(effect.door)}.")
        } else {
          Some(s"${kind(user)} closes ${kind(effect.door)}.")
        }
      })) getOrElse {
        if (effect.opened) {
          Some(s"${kind(effect.door)} opens.")
        } else {
          Some(s"${kind(effect.door)} closes.")
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
    } else if (fov contains effect.location) {
      Some(s"${kind(creature)} drops ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: EquipItemEffect): Option[String] = {
    val creature = effect.creature
    val item = effect.item

    if (effect.parent.exists(p => p.isInstanceOf[EquipItemEffect] || p.isInstanceOf[EquipWeaponsEffect])) {
      None
    } else if (creature == player) {
      Some(s"You equip ${kind(item)}.")
    } else if (fov contains effect.location) {
      Some(s"${kind(creature)} equips ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: EquipWeaponsEffect): Option[String] = {
    if (effect.creature == player) {
      Some(s"You switch weapons.")
    } else {
      None
    }
  }

  private def build(effect: ExplodeEffect): Option[String] = {
    if (fov contains effect.location) {
      kind(effect.explosive) map (explosive => s"$explosive explodes.")
    } else {
      None
    }
  }

  private def build(effect: ExplosionHitEffect): Option[Event] = {
    val message = if (effect.target == player) {
      Some(if (effect.result.damage.isEmpty) {
        "You are hit by explosion without any damage."
      } else {
        "You are hit by explosion."
      })
    } else if (fov contains effect.location) {
      Some(if (effect.result.damage.isEmpty) {
        s"${kind(effect.target)} is hit by explosion without any damage."
      } else {
        s"${kind(effect.target)} is hit by explosion."
      })
    } else {
      None
    }

    message map (HitEvent(effect.target, effect.location, _))
  }

  private def build(effect: ExplosionLocationEffect): Option[Event] = {
    if (fov contains effect.location) {
      Some(ExplosionEvent(effect.location))
    } else {
      None
    }
  }

  private def build(effect: ExplosionMissEffect): Option[String] = {
    if (effect.target == player) {
      Some("You evade explosion.")
    } else {
      None
    }
  }

  private def build(effect: ExplosiveTimerEffect): Option[String] = {
    if ((effect.explosive(s).owner flatMap (_ (s))) exists (_.id == player)) {
      kind(effect.explosive) map (explosive => s"$explosive: ${effect.timer}")
    } else {
      None
    }
  }

  private def build(effect: GainConditionEffect): Option[String] = {
    if (effect.target == player) {
      Some(s"You are ${condition(effect.condition)}.")
    } else if (fov contains effect.location) {
      Some(s"${kind(effect.target)} is ${condition(effect.condition)}.")
    } else {
      None
    }
  }

  private def build(effect: GainLevelEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      Some("You feel stronger.")
    } else if (fov contains effect.location) {
      Some(s"${kind(target)} looks stronger.")
    } else {
      None
    }
  }

  private def build(effect: HealEffect): Option[String] = {
    val target = effect.target

    if (target == player) {
      Some("You feel better.")
    } else if (fov contains effect.location) {
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
        s"You hit ${kind(effect.target)} without any damage."
      } else if (bypass.isDefined) {
        s"You hit ${kind(effect.target)} bypassing some of it's armor."
      } else {
        s"You hit ${kind(effect.target)}."
      })
    } else if (effect.target == player) {
      Some(if (damage.isEmpty) {
        s"${kind(effect.attacker)} hits you without any damage."
      } else if (bypass.isDefined) {
        s"${kind(effect.attacker)} hits you bypassing some of your armor."
      } else {
        s"${kind(effect.attacker)} hits you."
      })
    } else if (fov contains effect.location) {
      if (damage.isEmpty) {
        None
      } else {
        Some(s"${kind(effect.attacker)} hits ${kind(effect.target)}.")
      }
    } else {
      None
    }

    message map (HitEvent(effect.target, effect.location, _))
  }

  private def build(effect: HackedEffect): Option[String] = {
    if (effect.hacker == player) {
      if (effect.lock.sub.isDefined) {
        Some(s"You manage to hack ${kind(effect.target)} partially.")
      } else {
        Some(s"You manage to hack ${kind(effect.target)}!")
      }
    } else if (fov contains effect.location) {
      Some(s"${kind(effect.hacker)} hacks ${kind(effect.target)}.")
    } else {
      None
    }
  }

  private def build(effect: HackFailedEffect): Option[String] = {
    val level = effect.failure match {
      case HackRule.Failure | HackRule.CertainFailure => ""
      case HackRule.BadFailure => " badly"
      case HackRule.CriticalFailure => " critically"
    }

    if (effect.hacker == player) {
      if (effect.failure == HackRule.CertainFailure) {
        Some(s"Hacking ${kind(effect.target)} does not seem possible.")
      } else {
        Some(s"You fail$level at hacking ${kind(effect.target)}.")
      }
    } else if (fov contains effect.location) {
      Some(s"${kind(effect.hacker)} fails$level at hacking ${kind(effect.target)}.")
    } else {
      None
    }
  }

  private def build(effect: ItemCraftedEffect): Option[String] = {
    if (effect.craftsman == player) {
      Some(s"You craft ${kind(effect.item)}.")
    } else {
      None
    }
  }

  private def build(effect: LocalizedDescriptionEffect): Option[String] = {
    effect.description flatMap (description => {
      if (fov contains effect.location) {
        Some(description)
      } else {
        None
      }
    })
  }

  private def build(effect: LockedUsableEffect): Option[String] = {
    if (effect.user == player) {
      Some(s"${kind(effect.usable)} is locked.")
    } else {
      None
    }
  }

  private def build(effect: LoseConditionEffect): Option[String] = {
    if (effect.target == player) {
      Some(s"You are no longer ${condition(effect.source.condition)}.")
    } else if (fov contains effect.location) {
      Some(s"${kind(effect.target)} is no longer ${condition(effect.source.condition)}.")
    } else {
      None
    }
  }

  private def build(effect: MachineryActivatedEffect): Option[String] = {
    if (effect.activator == player || (fov contains effect.location)) {
      effect.description
    } else {
      None
    }
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

  private def build(effect: MovedEffect): Option[Event] = {
    val target = effect.target

    if (target == player) {
      val items = getLocationVisibleItems(s, effect.target)(effect.to)
      val messages = items map (item => s"${kind(item)}.")

      if (messages.nonEmpty) {
        Some(GenericEvent(messages.mkString(" ")))
      } else {
        None
      }
    } else if (target(s).traits.missile.isDefined) {
      if ((fov contains effect.from) || (fov contains effect.to)) {
        Some(MoveMissileEvent(effect.from, effect.to))
      } else {
        None
      }
    } else {
      None
    }
  }

  private def build(effect: NoResponseEffect): Option[String] = {
    val source = effect.source
    val target = effect.target

    if (target == player) {
      Some(s"${kind(source)} does not respond.")
    } else {
      None
    }
  }

  private def build(effect: PowerUsedEffect): Option[String] = {
    if (effect.user == player) {
      effect.description
    } else {
      None
    }
  }

  private def build(effect: PickItemEffect): Option[String] = {
    val creature = effect.picker
    val item = effect.target

    if (creature == player) {
      Some(s"You pick up ${kind(item)}.")
    } else if (fov contains effect.location) {
      Some(s"${kind(creature)} picks up ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: ReceiveCommunicationEffect): Option[Event] = {
    val source = effect.source
    val target = effect.target

    if (target == player) {
      val communication = effect.communication(s)
      val message = s"""${kind(source)}: "${communication.message}""""

      Some(CommunicationEvent(effect.source, message, communication.validChoices(player)(s)))
    } else if (fov contains effect.location) {
      source match {
        case creature: CreatureId => Some(GenericEvent(s"""${kind(creature)} talks to ${kind(target)}.""""))
        case _ => None
      }
    } else {
      None
    }
  }

  private def build(effect: ReceiveKeyEffect): Option[String] = {
    if (effect.target == player) {
      effect.key match {
        case key: SharedKey => Some(s"You acquire ${kind(key.kind)}.")
        case _ => None
      }
    } else {
      None
    }
  }

  private def build(effect: RecycleItemEffect): Option[String] = {
    if (effect.recycler == player) {
      Some(s"You recycle ${kind(effect.item)}.")
    } else {
      None
    }
  }

  private def build(effect: RemoveEntityEffect): Option[String] = {
    effect.description flatMap (description => effect.location flatMap (location => {
      if (fov contains location) {
        val target = kind(effect.target)
        val result = target map parse(description, EventBuilder.PARAM_TARGET) getOrElse description

        Some(result)
      } else {
        None
      }
    }))
  }

  private def build(effect: ResistConditionEffect): Option[String] = {
    if (effect.target == player) {
      Some(s"You resist ${condition(effect.condition, noun = true)}.")
    } else {
      None
    }
  }

  private def build(effect: SignaledEffect): Option[Event] = {
    val signal = effect.signal

    if (!fov.contains(signal.location) && signal.owner.forall(_ == player(s).faction)) {
      calculateSignal(signal) map SignalEvent
    } else {
      None
    }
  }

  private def build(effect: ShootMissileEffect): Option[String] = {
    val attacker = effect.attacker

    if (attacker == player) {
      Some("You fire missile.")
    } else if (fov contains effect.sourceLocation) {
      Some(s"${kind(attacker)} fires missile.")
    } else {
      None
    }
  }


  private def build(effect: ShortageEffect): Option[String] = {
    if (effect.target == player) {
      if (effect.energy && effect.materials) {
        Some("Not enough energy or materials.")
      } else if (effect.energy) {
        Some("Not enough energy.")
      } else if (effect.materials) {
        Some("Not enough materials.")
      } else if (effect.components) {
        Some("Not enough components.")
      } else {
        None
      }
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

  private def build(effect: TransformBlockedEffect): Option[String] = {
    if (effect.owner.contains(player)) {
      Some("Not enough space.")
    } else {
      None
    }
  }

  private def build(effect: TrapHitEffect): Option[Event] = {
    if (effect.target == player || (fov contains effect.location)) {
      val description = effect.result.damage map (_ => effect.description) getOrElse effect.deflectDescription

      description map (description => {
        val target = if (effect.target == player) "you" else kind(effect.target)
        val message = parse(description, EventBuilder.PARAM_TARGET)(target)

        HitEvent(effect.target, effect.location, message)
      })
    } else {
      None
    }
  }

  private def build(effect: TrapMissEffect): Option[String] = {
    if (effect.target == player || fov.contains(effect.location)) {
      effect.description map (description => {
        val target = if (effect.target == player) "you" else kind(effect.target)

        parse(description, EventBuilder.PARAM_TARGET)(target)
      })
    } else {
      None
    }
  }

  private def build(effect: UnequipItemEffect): Option[String] = {
    val creature = effect.creature
    val item = effect.item

    if (effect.parent.exists(p => p.isInstanceOf[EquipItemEffect] || p.isInstanceOf[EquipWeaponsEffect])) {
      None
    } else if (creature == player) {
      Some(s"You unequip ${kind(item)}.")
    } else if (fov contains effect.location) {
      Some(s"${kind(creature)} unequips ${kind(item)}.")
    } else {
      None
    }
  }

  private def build(effect: UseUsableEffect): Option[String] = {
    if (effect.user == player) {
      Some(s"You use ${kind(effect.target)}.")
    } else if (fov contains effect.location) {
      Some(s"${kind(effect.user)} uses ${kind(effect.target)}.")
    } else {
      None
    }
  }

  private def condition(condition: Condition, noun: Boolean = false): String = {
    if (noun) {
      condition.key match {
        case "Conditions.Disoriented" => "disorientation"
        case "Conditions.Immobilized" => "immobilization"
        case key => key.split('.')(1).toLowerCase()
      }
    } else {
      condition.key match {
        case key => key.split('.')(1).toLowerCase()
      }
    }
  }

  private def hasParentEffect(effect: Effect, matches: Effect => Boolean): Boolean = {
    def check(effect: Effect): Boolean = {
      if (matches(effect)) {
        true
      } else {
        effect.parent exists check
      }
    }

    check(effect)
  }

  private def kind(mixed: KindId): Option[String] = {
    mixed match {
      case t: CreatureKindId => Some(kind(t))
      case t: ItemKindId => Some(kind(t))
      case t: WallKindId => Some(kind(t))
      case t: WidgetKindId => Some(kind(t))
      case _ => None
    }
  }

  private def kind(mixed: EntityId): Option[String] = {
    mixed match {
      case t: ContainerId => getContainerItems(s)(t).headOption map kind
      case t: CreatureId => Some(kind(t))
      case t: ItemId => Some(kind(t))
      case t: WallId => Some(kind(t))
      case _ => None
    }
  }

  private def kind(lockable: LockableId): String = {
    lockable match {
      case creature: CreatureId => kind(creature)
      case item: ItemId => kind(item)
    }
  }

  private def kind(usable: UsableId): String = {
    usable match {
      case creature: CreatureId => kind(creature)
      case item: ItemId => kind(item)
    }
  }

  private def kind(creature: CreatureId): String = {
    s.assets.kinds.creatures(creature(s).kind).name
  }

  private def kind(creature: CreatureKindId): String = {
    s.assets.kinds.creatures(creature).name
  }

  private def kind(item: ItemId): String = {
    s.assets.kinds.items(item(s).kind).name
  }

  private def kind(item: ItemKindId): String = {
    s.assets.kinds.items(item).name
  }

  private def kind(terrain: TerrainId): String = {
    s.assets.kinds.terrains(terrain(s).kind).name
  }

  private def kind(wall: WallId): String = {
    s.assets.kinds.walls(wall(s).kind).name
  }

  private def kind(wall: WallKindId): String = {
    s.assets.kinds.walls(wall).name
  }

  private def kind(widget: WidgetKindId): String = {
    val item = s.assets.kinds.widgets(widget).item

    kind(item)
  }

  private def kind(key: KeyKindId): String = {
    s.assets.keys(key).name
  }

  private def parse(description: String, parameter: String)(value: String): String = {
    description.replace(s"{$parameter}", value)
  }
}
