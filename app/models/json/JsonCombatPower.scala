package models.json

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, WidgetKind, WidgetKindId}
import models.json.JsonBase.mapFormat
import play.api.libs.json._

object JsonCombatPower {

  import JsonBase.{mapReads, polymorphicObjectFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val equipmentCategoryFormat = JsonItemEquipment.categoryFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat
  lazy private implicit val widgetKindCategoryFormat = JsonWidgetKind.categoryFormat
  implicitly(mapReads[CreatureKindId, Int])
  implicitly(mapReads[ItemKindId, Int])
  implicitly(mapReads[Equipment.Category, Map[ItemKindId, Int]])
  implicitly(mapReads[WidgetKindId, Int])
  implicitly(mapReads[WidgetKind.Category, Map[WidgetKindId, Int]])

  lazy val combatPowerWrites: Writes[CombatPower] = Json.writes[CombatPower]

  lazy implicit val categoryFormat: Format[CombatPower.Category] = polymorphicObjectFormat({
    case "CombatPower.Top" => CombatPower.Top
    case "CombatPower.High" => CombatPower.High
    case "CombatPower.Medium" => CombatPower.Medium
    case "CombatPower.Low" => CombatPower.Low
  })
}
