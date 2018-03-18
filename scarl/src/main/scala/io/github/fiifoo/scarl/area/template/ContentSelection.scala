package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._

sealed trait ContentSelection

case object ContentSelection {

  sealed trait CreatureSelection extends ContentSelection

  sealed trait ItemSelection extends ContentSelection

  sealed trait TemplateSelection extends ContentSelection

  sealed trait WidgetSelection extends ContentSelection

  case class FixedCreature(kind: CreatureKindId) extends CreatureSelection

  case class FixedItem(kind: ItemKindId) extends ItemSelection

  case class FixedTemplate(template: TemplateId) extends TemplateSelection

  case class FixedWidget(kind: WidgetKindId) extends WidgetSelection

  case class ThemeCreature(power: Set[CombatPower.Category] = Set()) extends CreatureSelection

  case class ThemeItem(category: Set[ItemKind.Category] = Set(),
                       power: Set[CombatPower.Category] = Set()
                      ) extends ItemSelection

  case class ThemeEquipment(category: Set[Equipment.Category] = Set(),
                            power: Set[CombatPower.Category] = Set()
                           ) extends ItemSelection

  case class ThemeTemplate(category: Set[Template.Category] = Set(),
                           power: Set[CombatPower.Category] = Set()
                          ) extends TemplateSelection

  case class ThemeWidget(category: Set[WidgetKind.Category] = Set(),
                         power: Set[CombatPower.Category] = Set()
                        ) extends WidgetSelection

}
