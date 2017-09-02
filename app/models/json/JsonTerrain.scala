package models.json

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.{Terrain, TerrainId}
import play.api.libs.json._

object JsonTerrain {

  import JsonBase.integerIdFormat

  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat

  lazy implicit val terrainIdFormat: Format[TerrainId] = integerIdFormat(_.value, TerrainId.apply)

  lazy val terrainFormat: Format[Terrain] = Json.format[Terrain]
}
