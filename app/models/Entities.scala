package models

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity._
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}

object Entities {

  case class ClientEntities(containers: Iterable[Container],
                            creatures: Iterable[Creature],
                            items: Iterable[Item],
                            statuses: Iterable[Status],
                            terrains: Iterable[Terrain],
                            walls: Iterable[Wall]
                           )

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

  implicit val clientEntitiesWrites = Json.writes[ClientEntities]

  def toClient(entities: Map[EntityId, Entity]): JsValue = {
    Json.toJson(ClientEntities(
      entities.values.collect { case e: Container => e },
      entities.values.collect { case e: Creature => e },
      entities.values.collect { case e: Item => e },
      entities.values.collect { case e: Status => e },
      entities.values.collect { case e: Terrain => e },
      entities.values.collect { case e: Wall => e }
    ))
  }
}
