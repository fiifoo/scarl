package models.json

import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import play.api.libs.json._

object JsonTheme {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat

  lazy private implicit val creatureCatalogueIdFormat = JsonCatalogues.creatureCatalogueIdFormat
  lazy private implicit val itemCatalogueIdFormat = JsonCatalogues.itemCatalogueIdFormat
  lazy private implicit val templateCatalogueIdFormat = JsonWorldCatalogues.templateCatalogueIdFormat
  lazy private implicit val terrainCatalogueIdFormat = JsonCatalogues.terrainCatalogueIdFormat
  lazy private implicit val wallCatalogueIdFormat = JsonCatalogues.wallCatalogueIdFormat
  lazy private implicit val widgetCatalogueIdFormat = JsonCatalogues.widgetCatalogueIdFormat

  lazy implicit val themeIdFormat: Format[ThemeId] = stringIdFormat(_.value, ThemeId.apply)

  lazy implicit val themeReads: Reads[Theme] = Json.reads

  lazy val themeMapReads: Reads[Map[ThemeId, Theme]] = mapReads
}
