package models.json

import io.github.fiifoo.scarl.core.kind.TerrainKind.Category
import io.github.fiifoo.scarl.core.kind.{TerrainKind, TerrainKindId}
import models.json.JsonBase.polymorphicObjectFormat
import play.api.libs.json._

object JsonTerrainKind {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val charFormat = JsonBase.charFormat

  lazy implicit val terrainKindIdFormat: Format[TerrainKindId] = stringIdFormat(_.value, TerrainKindId.apply)

  lazy implicit val terrainKindFormat: Format[TerrainKind] = Json.format

  lazy val terrainKindMapReads: Reads[Map[TerrainKindId, TerrainKind]] = mapReads

  lazy val terrainCategoryFormat: Format[Category] = polymorphicObjectFormat({
    case "TerrainKind.DefaultCategory" => TerrainKind.DefaultCategory
    case "TerrainKind.ConstructedCategory" => TerrainKind.ConstructedCategory
    case "TerrainKind.ImpassableCategory" => TerrainKind.ImpassableCategory
    case "TerrainKind.NaturalCategory" => TerrainKind.NaturalCategory
  })
}
