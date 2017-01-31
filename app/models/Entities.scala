package models

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity._
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}

object Entities {

  implicit val entityIdWrites = new Writes[EntityId] {
    def writes(id: EntityId) = JsNumber(id.value)
  }

  implicit val locationWrites = Json.writes[Location]
  implicit val containerWrites = Json.writes[Container]
  implicit val creatureWrites = Json.writes[Creature]
  implicit val itemWrites = Json.writes[Item]
  implicit val statusWrites = new Writes[Status] {
    def writes(status: Status) = Json.obj(
      "id" -> status.id,
      "target" -> status.target,
      "type" -> status.getClass.getSimpleName
    )
  }
  implicit val terrainWrites = Json.writes[Terrain]
  implicit val wallWrites = Json.writes[Wall]

  def toJson(creature: Creature): JsValue = {
    Json.toJson(creature)
  }

  object Fov {

    case class LocationEntities(creature: Option[Creature],
                                terrain: Option[Terrain],
                                wall: Option[Wall]
                               )

    case class LocationData(location: Location,
                            entities: LocationEntities
                           )

    implicit val locationEntitiesWrites = Json.writes[LocationEntities]
    implicit val locationDataWrites = Json.writes[LocationData]

    var prev: Map[Location, LocationEntities] = Map()

    def toJson(fov: Map[Location, LocationEntities]): JsValue = {
      Json.toJson(fov map (x => LocationData(x._1, x._2)))
    }

    def toJson(fov: Iterable[Location]): JsValue = {
      Json.toJson(fov)
    }
  }

}
