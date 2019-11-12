package models.json

import io.github.fiifoo.scarl.area.feature._
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.ContentSelection.ItemSelection
import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.{ConduitLocations, Entrances, Routing}
import io.github.fiifoo.scarl.area.template.Template._
import io.github.fiifoo.scarl.area.template._
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonTemplate {

  import JsonBase.{mapReads, optionReads, polymorphicObjectFormat, polymorphicTypeReads, stringIdFormat}

  lazy private implicit val contentSourceCatalogueIdFormat = JsonWorldCatalogues.contentSourceCatalogueIdFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val locationReads = Json.reads[Location]
  lazy private implicit val mechanismFormat = JsonMechanism.mechanismFormat
  lazy private implicit val machinerySourceReads = Json.reads[MachinerySource]
  lazy private implicit val templateSourceCatalogueIdFormat = JsonWorldCatalogues.templateSourceCatalogueIdFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat

  lazy private implicit val creatureSelectionReads = JsonContentSelection.creatureSelectionReads
  lazy private implicit val itemSelectionReads = JsonContentSelection.itemSelectionReads
  lazy private implicit val terrainSelectionReads = JsonContentSelection.terrainSelectionReads
  lazy private implicit val templateSelectionReads = JsonContentSelection.templateSelectionReads
  lazy private implicit val wallSelectionReads = JsonContentSelection.wallSelectionReads
  lazy private implicit val widgetSelectionReads = JsonContentSelection.widgetSelectionReads

  implicitly(optionReads[ItemSelection])
  lazy private implicit val entrancesReads = Json.reads[Entrances]
  lazy private implicit val conduitLocationsFormat = Json.format[ConduitLocations]
  lazy private implicit val routingReads = Json.reads[Routing]

  lazy private implicit val rectangleReads = Json.reads[Rectangle]
  lazy private implicit val shapeReads: Reads[Shape] = polymorphicTypeReads(data => {
    case "Rectangle" => data.as[Rectangle]
  })

  lazy private implicit val creatureSourceReads = JsonContentSource.creatureSourceReads
  lazy private implicit val itemSourceReads = JsonContentSource.itemSourceReads
  lazy private implicit val templateSourceReads = JsonContentSource.templateSourceReads
  lazy private implicit val widgetSourceReads = JsonContentSource.widgetSourceReads

  lazy private implicit val burrowFeatureReads = Json.reads[BurrowFeature]
  lazy private implicit val houseFeatureReads = Json.reads[HouseFeature]
  lazy private implicit val randomizedContentFeatureReads = Json.reads[RandomizedContentFeature]
  lazy private implicit val trapRoomFeatureReads = Json.reads[TrapRoomFeature]
  lazy private implicit val featureReads: Reads[Feature] = polymorphicTypeReads(data => {
    case "BurrowFeature" => data.as[BurrowFeature]
    case "HouseFeature" => data.as[HouseFeature]
    case "RandomizedContentFeature" => data.as[RandomizedContentFeature]
    case "TrapRoomFeature" => data.as[TrapRoomFeature]
  })

  lazy private implicit val fixedContentReads = Json.reads[FixedContent]
  lazy private implicit val fixedTemplateReads = Json.reads[FixedTemplate]
  lazy private implicit val randomizedTemplateReads = Json.reads[RandomizedTemplate]
  lazy private implicit val sequenceTemplateReads = Json.reads[SequenceTemplate]

  lazy implicit val templateIdFormat: Format[TemplateId] = stringIdFormat(_.value, TemplateId.apply)

  lazy implicit val templateReads: Reads[Template] = polymorphicTypeReads(data => {
    case "FixedTemplate" => data.as[FixedTemplate]
    case "RandomizedTemplate" => data.as[RandomizedTemplate]
    case "SequenceTemplate" => data.as[SequenceTemplate]
  })

  lazy val templateMapReads: Reads[Map[TemplateId, Template]] = mapReads

  lazy val templateCategoryFormat: Format[Category] = polymorphicObjectFormat({
    case "Template.ChallengeCategory" => ChallengeCategory
    case "Template.RoomCategory" => RoomCategory
    case "Template.SpaceCategory" => SpaceCategory
    case "Template.StorageCategory" => StorageCategory
    case "Template.TrapCategory" => TrapCategory
  })
}
