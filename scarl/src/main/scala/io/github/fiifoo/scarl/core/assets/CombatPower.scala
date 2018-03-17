package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, WidgetKind, WidgetKindId}

object CombatPower {
  type Average = Map[CreatureKindId, Int]
  type Opposed = Map[CreatureKindId, Map[CreatureKindId, Int]]
  type Equipment = Map[Equipment.Category, Map[ItemKindId, Int]]
  type Widget = Map[WidgetKind.Category, Map[WidgetKindId, Int]]

  sealed trait Category

  case object Low extends Category

  case object Medium extends Category

  case object High extends Category

  case object Top extends Category

  val categories: Set[Category] = Set(
    Low,
    Medium,
    High,
    Top
  )

}

case class CombatPower(average: CombatPower.Average = Map(),
                       opposed: CombatPower.Opposed = Map(),
                       equipment: CombatPower.Equipment = Map(),
                       widget: CombatPower.Widget = Map(),
                      )
