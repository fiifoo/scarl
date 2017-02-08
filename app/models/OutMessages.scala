package models

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.game.{LocationEntities, OutMessage}
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}

object OutMessages {

  case class LocationData(location: Location,
                          entities: LocationEntities
                         )

  implicit val entityIdWrites = new Writes[EntityId] {
    def writes(id: EntityId) = JsNumber(id.value)
  }

  implicit val locationWrites = Json.writes[Location]
  implicit val creatureWrites = Json.writes[Creature]
  implicit val terrainWrites = Json.writes[Terrain]
  implicit val wallWrites = Json.writes[Wall]

  implicit val locationEntitiesWrites = Json.writes[LocationEntities]
  implicit val locationDataWrites = Json.writes[LocationData]
  implicit val locationMapWrites = new Writes[Map[Location, LocationEntities]] {
    def writes(x: Map[Location, LocationEntities]) = Json.toJson(x map (y => LocationData(y._1, y._2)))
  }
  implicit val fovWrites = Json.writes[OutMessage.Fov]
  implicit val messageWrites = Json.writes[OutMessage]

  def toJson(message: OutMessage): JsValue = {
    Json.toJson(message)
  }
}
