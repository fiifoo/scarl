package models.json

import io.github.fiifoo.scarl.area.feature.RandomizedContentFeature.{CreatureSource, ItemSource, WidgetSource}
import io.github.fiifoo.scarl.area.feature.{Feature, RandomizedContentFeature}
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.area.template._
import io.github.fiifoo.scarl.area.theme.ContentSelection.{FixedCreature, FixedItem, ThemeCreature, ThemeEquipment}
import io.github.fiifoo.scarl.area.theme.{CreatureSelection, ItemSelection}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.ItemKindId
import play.api.libs.json._

object JsonTemplate {

  import JsonBase.{mapReads, optionReads, polymorphicTypeReads, stringIdFormat, tuple2Format, tuple3Format}

  lazy private implicit val equipmentCategoryFormat = JsonItemEquipment.categoryFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val distributionFormat = JsonDistribution.distributionFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val locationReads = Json.reads[Location]
  lazy private implicit val mechanismFormat = JsonMechanism.mechanismFormat
  lazy private implicit val machinerySourceReads = Json.reads[MachinerySource]
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  implicitly(optionReads[ItemKindId])
  implicitly(tuple2Format[Int, Int])
  implicitly(tuple3Format[TemplateId, Int, Int])

  lazy private implicit val combatPowerCategoryFormat = JsonCombatPower.categoryFormat
  lazy private implicit val rectangleReads = Json.reads[Rectangle]
  lazy private implicit val shapeReads: Reads[Shape] = polymorphicTypeReads(data => {
    case "Rectangle" => data.as[Rectangle]
  })

  lazy private implicit val themeCreatureReads = Json.reads[ThemeCreature]
  lazy private implicit val fixedCreatureReads = Json.reads[FixedCreature]
  lazy private implicit val creatureSelectionReads: Reads[CreatureSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeCreature" => data.as[ThemeCreature]
    case "ContentSelection.FixedCreature" => data.as[FixedCreature]
  })
  lazy private implicit val themeEquipmentReads = Json.reads[ThemeEquipment]
  lazy private implicit val fixedItemReads = Json.reads[FixedItem]
  lazy private implicit val itemSelectionReads: Reads[ItemSelection] = polymorphicTypeReads(data => {
    case "ContentSelection.ThemeEquipment" => data.as[ThemeEquipment]
    case "ContentSelection.FixedItem" => data.as[FixedItem]
  })

  lazy private implicit val creatureSourceReads = Json.reads[CreatureSource]
  lazy private implicit val itemSourceReads = Json.reads[ItemSource]
  lazy private implicit val widgetSourceReads = Json.reads[WidgetSource]
  lazy private implicit val randomizedContentFeatureReads = Json.reads[RandomizedContentFeature]
  lazy private implicit val featureReads: Reads[Feature] = polymorphicTypeReads(data => {
    case "RandomizedContentFeature" => data.as[RandomizedContentFeature]
  })

  lazy private implicit val fixedContentReads = Json.reads[FixedContent]
  lazy private implicit val fixedTemplateReads = Json.reads[FixedTemplate]
  lazy private implicit val randomizedTemplateReads = Json.reads[RandomizedTemplate]

  lazy implicit val templateIdFormat: Format[TemplateId] = stringIdFormat(_.value, TemplateId.apply)

  lazy implicit val templateReads: Reads[Template] = polymorphicTypeReads(data => {
    case "FixedTemplate" => data.as[FixedTemplate]
    case "RandomizedTemplate" => data.as[RandomizedTemplate]
  })

  lazy val templateMapReads: Reads[Map[TemplateId, Template]] = mapReads
}
