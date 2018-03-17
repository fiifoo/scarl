package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId}

sealed trait ContentSelection

case object ContentSelection {

  sealed trait CreatureSelection extends ContentSelection

  sealed trait ItemSelection extends ContentSelection

  case class ThemeCreature(power: Set[CombatPower.Category] = Set()) extends CreatureSelection

  case class FixedCreature(kind: CreatureKindId) extends CreatureSelection

  case class ThemeEquipment(category: Set[Equipment.Category] = Set(),
                            power: Set[CombatPower.Category] = Set()
                           ) extends ItemSelection

  case class FixedItem(kind: ItemKindId) extends ItemSelection

}