package models

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.game.{LocationEntities, OutMessage, Statistics}
import play.api.libs.json._

object OutMessages {

  case class LocationData(location: Location,
                          entities: LocationEntities
                         )

  implicit val charWrites = new Writes[Char] {
    def writes(c: Char) = JsString(c.toString)
  }

  implicit val entityIdWrites = new Writes[EntityId] {
    def writes(id: EntityId) = JsNumber(id.value)
  }
  implicit val factionIdWrites = new Writes[FactionId] {
    def writes(id: FactionId) = JsString(id.value)
  }
  implicit val kindIdWrites = new Writes[KindId] {
    def writes(id: KindId) = JsString(id.value)
  }
  implicit val areaIdWrites = new Writes[AreaId] {
    def writes(id: AreaId) = JsString(id.value)
  }

  implicit val locationWrites = Json.writes[Location]
  implicit val creatureStatsWrites = Json.writes[Creature.Stats]
  implicit val creatureWrites = Json.writes[Creature]
  implicit val itemWrites = Json.writes[Item]
  implicit val terrainWrites = Json.writes[Terrain]
  implicit val wallWrites = Json.writes[Wall]

  implicit val locationEntitiesWrites = Json.writes[LocationEntities]
  implicit val locationDataWrites = Json.writes[LocationData]
  implicit val locationMapWrites = new Writes[Map[Location, LocationEntities]] {
    def writes(x: Map[Location, LocationEntities]) = Json.toJson(x map (y => LocationData(y._1, y._2)))
  }
  implicit val fovWrites = Json.writes[OutMessage.Fov]

  implicit val creatureKindWrites = Json.writes[CreatureKind]
  implicit val itemKindWrites = Json.writes[ItemKind]
  implicit val terrainKindWrites = Json.writes[TerrainKind]
  implicit val wallKindWrites = Json.writes[WallKind]
  implicit val kindsWrites = new Writes[Kinds] {
    def writes(k: Kinds) = JsObject(Map(
      "creatures" -> Json.toJson(k.creatures.values),
      "items" -> Json.toJson(k.items.values),
      "terrains" -> Json.toJson(k.terrains.values),
      "walls" -> Json.toJson(k.walls.values)
    ))
  }

  implicit val deathStatisticsWrites = new Writes[Map[CreatureKindId, Int]] {
    def writes(x: Map[CreatureKindId, Int]) = JsObject(x map { y => (y._1.value, JsNumber(y._2)) })
  }
  implicit val statisticsWrites = Json.writes[Statistics]

  implicit val messageWrites = Json.writes[OutMessage]

  def toJson(message: OutMessage): JsValue = {
    Json.toJson(message)
  }
}
