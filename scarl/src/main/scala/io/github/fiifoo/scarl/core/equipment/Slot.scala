package io.github.fiifoo.scarl.core.equipment

sealed trait Slot

sealed trait HandSlot extends Slot

sealed trait ArmorSlot extends Slot

case object MainHand extends HandSlot

case object OffHand extends HandSlot

case object HeadArmor extends ArmorSlot

case object ChestArmor extends ArmorSlot

case object HandArmor extends ArmorSlot

case object LegArmor extends ArmorSlot

case object FootArmor extends ArmorSlot
