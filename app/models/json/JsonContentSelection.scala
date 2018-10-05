package models.json

import io.github.fiifoo.scarl.area.template.ContentSelection
import play.api.libs.json._

object JsonContentSelection {

  import JsonBase.polymorphicTypeReads

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val creatureCategoryFormat = JsonCreatureKind.creatureCategoryFormat
  lazy private implicit val doorCategoryFormat = JsonItemKind.doorCategoryFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemCategoryFormat = JsonItemKind.itemCategoryFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat
  lazy private implicit val templateCategoryFormat = JsonTemplate.templateCategoryFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val terrainCategoryFormat = JsonTerrainKind.terrainCategoryFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val wallCategoryFormat = JsonWallKind.wallCategoryFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat
  lazy private implicit val widgetCategoryFormat = JsonWidgetKind.widgetCategoryFormat

  lazy private implicit val combatPowerCategoryFormat = JsonCombatPower.categoryFormat
  lazy private implicit val equipmentCategoryFormat = JsonItemEquipment.categoryFormat

  lazy private implicit val themeCreatureReads = Json.reads[ContentSelection.ThemeCreature]
  lazy private implicit val fixedCreatureReads = Json.reads[ContentSelection.FixedCreature]
  lazy val creatureSelectionReads: Reads[ContentSelection.CreatureSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeCreature" => data.as[ContentSelection.ThemeCreature]
    case "ContentSelection.FixedCreature" => data.as[ContentSelection.FixedCreature]
  })

  lazy private implicit val themeDoorReads = Json.reads[ContentSelection.ThemeDoor]
  lazy private implicit val themeItemReads = Json.reads[ContentSelection.ThemeItem]
  lazy private implicit val themeEquipmentReads = Json.reads[ContentSelection.ThemeEquipment]
  lazy private implicit val fixedDoorReads = Json.reads[ContentSelection.FixedDoor]
  lazy private implicit val fixedItemReads = Json.reads[ContentSelection.FixedItem]
  lazy val itemSelectionReads: Reads[ContentSelection.ItemSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeDoor" => data.as[ContentSelection.ThemeDoor]
    case "ContentSelection.ThemeItem" => data.as[ContentSelection.ThemeItem]
    case "ContentSelection.ThemeEquipment" => data.as[ContentSelection.ThemeEquipment]
    case "ContentSelection.FixedDoor" => data.as[ContentSelection.FixedDoor]
    case "ContentSelection.FixedItem" => data.as[ContentSelection.FixedItem]
  })

  lazy private implicit val themeTemplateSelectionReads = Json.reads[ContentSelection.ThemeTemplate]
  lazy private implicit val fixedTemplateSelectionReads = Json.reads[ContentSelection.FixedTemplate]
  lazy val templateSelectionReads: Reads[ContentSelection.TemplateSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeTemplate" => data.as[ContentSelection.ThemeTemplate]
    case "ContentSelection.FixedTemplate" => data.as[ContentSelection.FixedTemplate]
  })

  lazy private implicit val themeTerrainReads = Json.reads[ContentSelection.ThemeTerrain]
  lazy private implicit val fixedTerrainSelectionReads = Json.reads[ContentSelection.FixedTerrain]
  lazy val terrainSelectionReads: Reads[ContentSelection.TerrainSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.FixedTerrain" => data.as[ContentSelection.FixedTerrain]
    case "ContentSelection.ThemeTerrain" => data.as[ContentSelection.ThemeTerrain]
  })

  lazy private implicit val themeWallReads = Json.reads[ContentSelection.ThemeWall]
  lazy private implicit val fixedWallSelectionReads = Json.reads[ContentSelection.FixedWall]
  lazy val wallSelectionReads: Reads[ContentSelection.WallSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.FixedWall" => data.as[ContentSelection.FixedWall]
    case "ContentSelection.ThemeWall" => data.as[ContentSelection.ThemeWall]
  })

  lazy private implicit val themeWidgetReads = Json.reads[ContentSelection.ThemeWidget]
  lazy private implicit val fixedWidgetReads = Json.reads[ContentSelection.FixedWidget]
  lazy val widgetSelectionReads: Reads[ContentSelection.WidgetSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeWidget" => data.as[ContentSelection.ThemeWidget]
    case "ContentSelection.FixedWidget" => data.as[ContentSelection.FixedWidget]
  })
}
