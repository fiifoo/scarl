package models.json

import io.github.fiifoo.scarl.area.template.ContentSelection
import play.api.libs.json._

object JsonContentSelection {

  import JsonBase.polymorphicTypeReads

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val creatureCatalogueIdFormat = JsonCatalogues.creatureCatalogueIdFormat
  lazy private implicit val creatureCategoryFormat = JsonCreatureKind.creatureCategoryFormat
  lazy private implicit val doorCategoryFormat = JsonItemKind.doorCategoryFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemCatalogueIdFormat = JsonCatalogues.itemCatalogueIdFormat
  lazy private implicit val itemCategoryFormat = JsonItemKind.itemCategoryFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat
  lazy private implicit val templateCatalogueIdFormat = JsonWorldCatalogues.templateCatalogueIdFormat
  lazy private implicit val templateCategoryFormat = JsonTemplate.templateCategoryFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val terrainCatalogueIdFormat = JsonCatalogues.terrainCatalogueIdFormat
  lazy private implicit val terrainCategoryFormat = JsonTerrainKind.terrainCategoryFormat
  lazy private implicit val themeIdFormat = JsonTheme.themeIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val wallCatalogueIdFormat = JsonCatalogues.wallCatalogueIdFormat
  lazy private implicit val wallCategoryFormat = JsonWallKind.wallCategoryFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat
  lazy private implicit val widgetCatalogueIdFormat = JsonCatalogues.widgetCatalogueIdFormat
  lazy private implicit val widgetCategoryFormat = JsonWidgetKind.widgetCategoryFormat

  lazy private implicit val combatPowerCategoryFormat = JsonCombatPower.categoryFormat
  lazy private implicit val equipmentCategoryFormat = JsonItemEquipment.categoryFormat

  lazy private implicit val catalogueCreatureReads = Json.reads[ContentSelection.CatalogueCreature]
  lazy private implicit val fixedCreatureReads = Json.reads[ContentSelection.FixedCreature]
  lazy private implicit val themeCreatureReads = Json.reads[ContentSelection.ThemeCreature]
  lazy val creatureSelectionReads: Reads[ContentSelection.CreatureSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.CatalogueCreature" => data.as[ContentSelection.CatalogueCreature]
    case "ContentSelection.FixedCreature" => data.as[ContentSelection.FixedCreature]
    case "ContentSelection.ThemeCreature" => data.as[ContentSelection.ThemeCreature]
  })

  lazy private implicit val catalogueDoorReads = Json.reads[ContentSelection.CatalogueDoor]
  lazy private implicit val catalogueEquipmentReads = Json.reads[ContentSelection.CatalogueEquipment]
  lazy private implicit val catalogueItemReads = Json.reads[ContentSelection.CatalogueItem]
  lazy private implicit val fixedDoorReads = Json.reads[ContentSelection.FixedDoor]
  lazy private implicit val fixedItemReads = Json.reads[ContentSelection.FixedItem]
  lazy private implicit val themeDoorReads = Json.reads[ContentSelection.ThemeDoor]
  lazy private implicit val themeEquipmentReads = Json.reads[ContentSelection.ThemeEquipment]
  lazy private implicit val themeItemReads = Json.reads[ContentSelection.ThemeItem]
  lazy val itemSelectionReads: Reads[ContentSelection.ItemSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.CatalogueDoor" => data.as[ContentSelection.CatalogueDoor]
    case "ContentSelection.CatalogueEquipment" => data.as[ContentSelection.CatalogueEquipment]
    case "ContentSelection.CatalogueItem" => data.as[ContentSelection.CatalogueItem]
    case "ContentSelection.FixedDoor" => data.as[ContentSelection.FixedDoor]
    case "ContentSelection.FixedItem" => data.as[ContentSelection.FixedItem]
    case "ContentSelection.ThemeDoor" => data.as[ContentSelection.ThemeDoor]
    case "ContentSelection.ThemeEquipment" => data.as[ContentSelection.ThemeEquipment]
    case "ContentSelection.ThemeItem" => data.as[ContentSelection.ThemeItem]
  })

  lazy private implicit val catalogueTemplateReads = Json.reads[ContentSelection.CatalogueTemplate]
  lazy private implicit val fixedTemplateReads = Json.reads[ContentSelection.FixedTemplate]
  lazy private implicit val themeTemplateReads = Json.reads[ContentSelection.ThemeTemplate]
  lazy val templateSelectionReads: Reads[ContentSelection.TemplateSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.CatalogueTemplate" => data.as[ContentSelection.CatalogueTemplate]
    case "ContentSelection.FixedTemplate" => data.as[ContentSelection.FixedTemplate]
    case "ContentSelection.ThemeTemplate" => data.as[ContentSelection.ThemeTemplate]
  })

  lazy private implicit val catalogueTerrainReads = Json.reads[ContentSelection.CatalogueTerrain]
  lazy private implicit val fixedTerrainReads = Json.reads[ContentSelection.FixedTerrain]
  lazy private implicit val themeTerrainReads = Json.reads[ContentSelection.ThemeTerrain]
  lazy val terrainSelectionReads: Reads[ContentSelection.TerrainSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.CatalogueTerrain" => data.as[ContentSelection.CatalogueTerrain]
    case "ContentSelection.FixedTerrain" => data.as[ContentSelection.FixedTerrain]
    case "ContentSelection.ThemeTerrain" => data.as[ContentSelection.ThemeTerrain]
  })

  lazy private implicit val catalogueWallReads = Json.reads[ContentSelection.CatalogueWall]
  lazy private implicit val fixedWallReads = Json.reads[ContentSelection.FixedWall]
  lazy private implicit val themeWallReads = Json.reads[ContentSelection.ThemeWall]
  lazy val wallSelectionReads: Reads[ContentSelection.WallSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.CatalogueWall" => data.as[ContentSelection.CatalogueWall]
    case "ContentSelection.FixedWall" => data.as[ContentSelection.FixedWall]
    case "ContentSelection.ThemeWall" => data.as[ContentSelection.ThemeWall]
  })

  lazy private implicit val catalogueWidgetReads = Json.reads[ContentSelection.CatalogueWidget]
  lazy private implicit val fixedWidgetReads = Json.reads[ContentSelection.FixedWidget]
  lazy private implicit val themeWidgetReads = Json.reads[ContentSelection.ThemeWidget]
  lazy val widgetSelectionReads: Reads[ContentSelection.WidgetSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.CatalogueWidget" => data.as[ContentSelection.CatalogueWidget]
    case "ContentSelection.FixedWidget" => data.as[ContentSelection.FixedWidget]
    case "ContentSelection.ThemeWidget" => data.as[ContentSelection.ThemeWidget]
  })
}
