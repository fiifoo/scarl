package models.json

import io.github.fiifoo.scarl.core.entity._
import models.json.FormatEntity._
import play.api.libs.json._

object FormatEntities {
  implicit val formatEntities = new Format[Map[EntityId, Entity]] {
    def writes(entities: Map[EntityId, Entity]) = JsObject(Map(
      "containers" -> Json.toJson(entities.values collect { case container: Container => container }),
      "creatures" -> Json.toJson(entities.values collect { case creature: Creature => creature }),
      "items" -> Json.toJson(entities.values collect { case item: Item => item }),
      "statuses" -> Json.toJson(entities.values collect { case status: Status => status }),
      "terrains" -> Json.toJson(entities.values collect { case terrain: Terrain => terrain }),
      "walls" -> Json.toJson(entities.values collect { case wall: Wall => wall })
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value

      val list =
        obj("containers").as[List[Container]] :::
          obj("creatures").as[List[Creature]] :::
          obj("items").as[List[Item]] :::
          obj("statuses").as[List[Status]] :::
          obj("terrains").as[List[Terrain]] :::
          obj("walls").as[List[Wall]]

      val map = list.map((x: Entity) => x.id -> x).toMap

      JsSuccess(map)
    }
  }
}
