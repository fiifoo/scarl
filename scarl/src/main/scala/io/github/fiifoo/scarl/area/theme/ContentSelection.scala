package io.github.fiifoo.scarl.area.theme

import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId}

sealed trait ContentSelection

sealed trait CreatureSelection extends ContentSelection

sealed trait ItemSelection extends ContentSelection

case object ContentSelection {

  case class ThemeCreature(minCombatPower: Int, maxCombatPower: Int) extends CreatureSelection

  case class FixedCreature(kind: CreatureKindId) extends CreatureSelection

  case object ThemeItem extends ItemSelection

  case class FixedItem(kind: ItemKindId) extends ItemSelection

}
