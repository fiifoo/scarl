package models.json

import io.github.fiifoo.scarl.game.LocationEntities
import play.api.libs.json._

object JsonLocationEntities {
  lazy private implicit val itemFormat = JsonItem.itemFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val creatureFormat = JsonCreature.creatureFormat
  lazy private implicit val terrainFormat = JsonTerrain.terrainFormat
  lazy private implicit val wallFormat = JsonWall.wallFormat

  lazy val locationEntitiesWrites: Writes[LocationEntities] = Json.writes[LocationEntities]
}
