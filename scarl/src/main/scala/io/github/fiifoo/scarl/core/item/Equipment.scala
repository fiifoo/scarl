package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.Item
import io.github.fiifoo.scarl.core.item.Equipment._

object Equipment {

  sealed trait Slot

  sealed trait HandSlot extends Slot

  sealed trait ArmorSlot extends Slot

  case object MainHand extends HandSlot

  case object OffHand extends HandSlot

  case object RangedSlot extends Slot

  case object HeadArmor extends ArmorSlot

  case object ChestArmor extends ArmorSlot

  case object HandArmor extends ArmorSlot

  case object LegArmor extends ArmorSlot

  case object FootArmor extends ArmorSlot

  def selectEquipment(slot: Slot, item: Item): Option[Equipment] = {
    val valid: PartialFunction[Equipment, Equipment] = {
      case e: Equipment if e.slots contains slot => e
    }

    // Order might matter eg. two handed weapon + shield
    List(
      item.weapon collect valid,
      item.rangedWeapon collect valid,
      item.shield collect valid,
      item.armor collect valid
    ).flatten.headOption
  }
}

sealed trait Equipment {
  val stats: Stats
  val slots: Set[Slot]
  val fillAll: Boolean
}

case class Armor(stats: Stats, slot: ArmorSlot) extends Equipment {
  val slots: Set[Slot] = Set(slot)
  val fillAll: Boolean = true
}

case class RangedWeapon(stats: Stats) extends Equipment {
  val slots: Set[Slot] = Set(RangedSlot)
  val fillAll: Boolean = true
}

case class Shield(stats: Stats) extends Equipment {
  val slots: Set[Slot] = Set(OffHand)
  val fillAll: Boolean = true
}

case class Weapon(stats: Stats, twoHanded: Boolean) extends Equipment {
  val slots: Set[Slot] = Set(MainHand, OffHand)
  val fillAll: Boolean = twoHanded
}
