package models.json

import io.github.fiifoo.scarl.area.feature.{Feature, RandomizedContentFeature}
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.area.template.Template.{Category, ChallengeCategory, TrapCategory}
import io.github.fiifoo.scarl.area.template.{ContentSelection, ContentSource, _}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.ItemKindId
import play.api.libs.json._

object JsonTemplate {

  import JsonBase.{mapReads, optionReads, polymorphicObjectFormat, polymorphicTypeReads, stringIdFormat, tuple2Format, tuple3Format}

  lazy private implicit val equipmentCategoryFormat = JsonItemEquipment.categoryFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val distributionFormat = JsonDistribution.distributionFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemKindCategoryFormat = JsonItemKind.categoryFormat
  lazy private implicit val locationReads = Json.reads[Location]
  lazy private implicit val mechanismFormat = JsonMechanism.mechanismFormat
  lazy private implicit val machinerySourceReads = Json.reads[MachinerySource]
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat
  lazy private implicit val widgetKindCategoryFormat = JsonWidgetKind.categoryFormat

  implicitly(optionReads[ItemKindId])
  implicitly(tuple2Format[Int, Int])
  implicitly(tuple3Format[Option[ItemKindId], Int, Int])

  lazy private implicit val combatPowerCategoryFormat = JsonCombatPower.categoryFormat
  lazy private implicit val rectangleReads = Json.reads[Rectangle]
  lazy private implicit val shapeReads: Reads[Shape] = polymorphicTypeReads(data => {
    case "Rectangle" => data.as[Rectangle]
  })

  lazy private implicit val themeCreatureReads = Json.reads[ContentSelection.ThemeCreature]
  lazy private implicit val fixedCreatureReads = Json.reads[ContentSelection.FixedCreature]
  lazy private implicit val creatureSelectionReads: Reads[ContentSelection.CreatureSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeCreature" => data.as[ContentSelection.ThemeCreature]
    case "ContentSelection.FixedCreature" => data.as[ContentSelection.FixedCreature]
  })
  lazy private implicit val themeItemReads = Json.reads[ContentSelection.ThemeItem]
  lazy private implicit val themeEquipmentReads = Json.reads[ContentSelection.ThemeEquipment]
  lazy private implicit val fixedItemReads = Json.reads[ContentSelection.FixedItem]
  lazy private implicit val itemSelectionReads: Reads[ContentSelection.ItemSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeItem" => data.as[ContentSelection.ThemeItem]
    case "ContentSelection.ThemeEquipment" => data.as[ContentSelection.ThemeEquipment]
    case "ContentSelection.FixedItem" => data.as[ContentSelection.FixedItem]
  })

  lazy private implicit val themeWidgetReads = Json.reads[ContentSelection.ThemeWidget]
  lazy private implicit val fixedWidgetReads = Json.reads[ContentSelection.FixedWidget]
  lazy private implicit val widgetSelectionReads: Reads[ContentSelection.WidgetSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeWidget" => data.as[ContentSelection.ThemeWidget]
    case "ContentSelection.FixedWidget" => data.as[ContentSelection.FixedWidget]
  })

  lazy private implicit val creatureSourceReads = Json.reads[ContentSource.CreatureSource]
  lazy private implicit val itemSourceReads = Json.reads[ContentSource.ItemSource]
  lazy private implicit val widgetSourceReads = Json.reads[ContentSource.WidgetSource]
  lazy private implicit val randomizedContentFeatureReads = Json.reads[RandomizedContentFeature]
  lazy private implicit val featureReads: Reads[Feature] = polymorphicTypeReads(data => {
    case "RandomizedContentFeature" => data.as[RandomizedContentFeature]
  })

  lazy private implicit val themeTemplateSelectionReads = Json.reads[ContentSelection.ThemeTemplate]
  lazy private implicit val fixedTemplateSelectionReads = Json.reads[ContentSelection.FixedTemplate]
  lazy private implicit val templateSelectionReads: Reads[ContentSelection.TemplateSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeTemplate" => data.as[ContentSelection.ThemeTemplate]
    case "ContentSelection.FixedTemplate" => data.as[ContentSelection.FixedTemplate]
  })
  lazy private implicit val templateSourceReads = Json.reads[ContentSource.TemplateSource]

  lazy private implicit val fixedContentReads = Json.reads[FixedContent]
  lazy private implicit val fixedTemplateReads = Json.reads[FixedTemplate]
  lazy private implicit val randomizedTemplateReads = Json.reads[RandomizedTemplate]

  lazy implicit val templateIdFormat: Format[TemplateId] = stringIdFormat(_.value, TemplateId.apply)

  lazy implicit val templateReads: Reads[Template] = polymorphicTypeReads(data => {
    case "FixedTemplate" => data.as[FixedTemplate]
    case "RandomizedTemplate" => data.as[RandomizedTemplate]
  })

  lazy implicit val categoryFormat: Format[Category] = polymorphicObjectFormat({
    case "Template.ChallengeCategory" => ChallengeCategory
    case "Template.TrapCategory" => TrapCategory
  })

  lazy val templateMapReads: Reads[Map[TemplateId, Template]] = mapReads
}
