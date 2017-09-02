package models.json

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.game.map.MapLocation
import play.api.libs.json._

object JsonMapLocation {

  import JsonBase.mapFormat

  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat

  lazy implicit val mapLocationFormat: Format[MapLocation] = Json.format

  lazy val mapLocationMapFormat: Format[Map[Location, MapLocation]] = mapFormat
}
