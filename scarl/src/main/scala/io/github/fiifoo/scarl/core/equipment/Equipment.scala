package io.github.fiifoo.scarl.core.equipment

import io.github.fiifoo.scarl.core.character.Stats

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
