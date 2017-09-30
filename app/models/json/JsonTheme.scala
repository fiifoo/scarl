package models.json

import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import play.api.libs.json._

object JsonTheme {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat

  lazy implicit val themeIdFormat: Format[ThemeId] = stringIdFormat(_.value, ThemeId.apply)

  lazy implicit val themeFormat: Format[Theme] = Json.format

  lazy val themeMapReads: Reads[Map[ThemeId, Theme]] = mapReads
}
