package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId}

sealed trait ContentSelection

sealed trait CreatureSelection extends ContentSelection

sealed trait ItemSelection extends ContentSelection

case object ContentSelection {

  case class ThemeCreature(power: Set[CombatPower.Category] = Set()) extends CreatureSelection

  case class FixedCreature(kind: CreatureKindId) extends CreatureSelection

  case class ThemeEquipment(category: Set[Equipment.Category] = Set(),
                            power: Set[CombatPower.Category] = Set()
                           ) extends ItemSelection

  case class FixedItem(kind: ItemKindId) extends ItemSelection

}
