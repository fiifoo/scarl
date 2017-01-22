package models

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.{Entity, EntityId}
import play.api.libs.json.{JsObject, JsValue, Json}

class Player(send: JsValue => Unit) {

  def receive(entities: Map[EntityId, Entity], fov: List[Location]): Unit = {

    implicit val locationWrites = Json.writes[Location]

    val data: Map[String, JsValue] = Map(
      "entities" -> Entities.toClient(entities),
      "fov" -> Json.toJson(fov)
    )

    send(JsObject(data))
  }
}
