package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.area.template.{Template, TemplateId}
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._

object CombatPower {
  type Average = Map[CreatureKindId, Int]
  type Opposed = Map[CreatureKindId, Map[CreatureKindId, Int]]
  type Equipment = Map[Equipment.Category, Map[ItemKindId, Int]]
  type Item = Map[ItemKind.Category, Map[ItemKindId, Int]]
  type Widget = Map[WidgetKind.Category, Map[WidgetKindId, Int]]
  type Template = Map[Template.Category, Map[TemplateId, Int]]

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

  val all = (0, 100)
}

case class CombatPower(average: CombatPower.Average = Map(),
                       opposed: CombatPower.Opposed = Map(),
                       equipment: CombatPower.Equipment = Map(),
                       item: CombatPower.Item = Map(),
                       widget: CombatPower.Widget = Map(),
                       template: CombatPower.Template = Map()
                      )
