package models.json

import io.github.fiifoo.scarl.area.template.{Template, TemplateId}
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._
import play.api.libs.json._

object JsonCombatPower {

  import JsonBase.{mapReads, polymorphicObjectFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val equipmentCategoryFormat = JsonItemEquipment.categoryFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemKindCategoryFormat = JsonItemKind.categoryFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat
  lazy private implicit val templateCategoryFormat = JsonTemplate.categoryFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat
  lazy private implicit val widgetKindCategoryFormat = JsonWidgetKind.categoryFormat
  implicitly(mapReads[CreatureKindId, Int])
  implicitly(mapReads[ItemKindId, Int])
  implicitly(mapReads[Equipment.Category, Map[ItemKindId, Int]])
  implicitly(mapReads[ItemKind.Category, Map[ItemKindId, Int]])
  implicitly(mapReads[TemplateId, Int])
  implicitly(mapReads[Template.Category, Map[TemplateId, Int]])
  implicitly(mapReads[WidgetKindId, Int])
  implicitly(mapReads[WidgetKind.Category, Map[WidgetKindId, Int]])

  lazy val combatPowerFormat: Format[CombatPower] = Json.format[CombatPower]

  lazy implicit val categoryFormat: Format[CombatPower.Category] = polymorphicObjectFormat({
    case "CombatPower.Top" => CombatPower.Top
    case "CombatPower.High" => CombatPower.High
    case "CombatPower.Medium" => CombatPower.Medium
    case "CombatPower.Low" => CombatPower.Low
  })
}
