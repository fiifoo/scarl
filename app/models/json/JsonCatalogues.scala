package models.json

import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import play.api.libs.json._

object JsonCatalogues {

  import JsonBase.{mapReads, stringIdFormat, weightedChoiceFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  lazy implicit val creatureCatalogueIdFormat: Format[CreatureCatalogueId] = stringIdFormat(_.value, CreatureCatalogueId.apply)
  lazy implicit val itemCatalogueIdFormat: Format[ItemCatalogueId] = stringIdFormat(_.value, ItemCatalogueId.apply)
  lazy implicit val terrainCatalogueIdFormat: Format[TerrainCatalogueId] = stringIdFormat(_.value, TerrainCatalogueId.apply)
  lazy implicit val wallCatalogueIdFormat: Format[WallCatalogueId] = stringIdFormat(_.value, WallCatalogueId.apply)
  lazy implicit val widgetCatalogueIdFormat: Format[WidgetCatalogueId] = stringIdFormat(_.value, WidgetCatalogueId.apply)

  implicitly(weightedChoiceFormat[CreatureKindId])

  lazy private implicit val creatureCatalogueReads: Reads[CreatureCatalogue] = Json.reads
  lazy private implicit val itemCatalogueReads: Reads[ItemCatalogue] = Json.reads
  lazy private implicit val terrainCatalogueReads: Reads[TerrainCatalogue] = Json.reads
  lazy private implicit val wallCatalogueReads: Reads[WallCatalogue] = Json.reads
  lazy private implicit val widgetCatalogueReads: Reads[WidgetCatalogue] = Json.reads

  lazy implicit val creaturesReads: Reads[Map[CreatureCatalogueId, CreatureCatalogue]] = mapReads
  lazy implicit val itemsReads: Reads[Map[ItemCatalogueId, ItemCatalogue]] = mapReads
  lazy implicit val terrainsReads: Reads[Map[TerrainCatalogueId, TerrainCatalogue]] = mapReads
  lazy implicit val wallsReads: Reads[Map[WallCatalogueId, WallCatalogue]] = mapReads
  lazy implicit val widgetsReads: Reads[Map[WidgetCatalogueId, WidgetCatalogue]] = mapReads
}
