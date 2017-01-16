package models

import io.github.fiifoo.scarl.core.entity.{Entity, EntityId}
import play.api.libs.json.JsValue

class Player(sendEntities: JsValue => Unit) {

  def receive(entities: Map[EntityId, Entity]): Unit = {
    val json = Entities.toClient(entities)
    sendEntities(json)
  }
}
