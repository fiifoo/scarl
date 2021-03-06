package models.json

import io.github.fiifoo.scarl.core.entity._
import play.api.libs.json._

object JsonEntity {

  import JsonBase.polymorphicIdFormat

  lazy private implicit val containerFormat = JsonContainer.containerFormat
  lazy private implicit val creatureFormat = JsonCreature.creatureFormat
  lazy private implicit val globalActorFormat = JsonGlobalActor.globalActorFormat
  lazy private implicit val itemFormat = JsonItem.itemFormat
  lazy private implicit val machineryFormat = JsonMachinery.machineryFormat
  lazy private implicit val statusFormat = JsonStatus.statusFormat
  lazy private implicit val terrainFormat = JsonTerrain.terrainFormat
  lazy private implicit val wallFormat = JsonWall.wallFormat

  lazy val entityIdFormat: Format[EntityId] = polymorphicIdFormat[EntityId, Int](
    value => {
      case "ActiveStatusId" => ActiveStatusId(value)
      case "ContainerId" => ContainerId(value)
      case "CreatureId" => CreatureId(value)
      case "GlobalActorId" => GlobalActorId(value)
      case "ItemId" => ItemId(value)
      case "MachineryId" => MachineryId(value)
      case "PassiveStatusId" => PassiveStatusId(value)
      case "TerrainId" => TerrainId(value)
      case "TriggerStatusId" => TriggerStatusId(value)
      case "WallId" => WallId(value)
    }, {
      id => JsNumber(id.value)
    }
  )

  lazy val usableIdFormat: Format[UsableId] = polymorphicIdFormat[UsableId, Int](
    value => {
      case "CreatureId" => CreatureId(value)
      case "ItemId" => ItemId(value)
    }, {
      id => JsNumber(id.value)
    }
  )

  lazy implicit val entityMapFormat = new Format[Map[EntityId, Entity]] {
    def writes(entities: Map[EntityId, Entity]) = JsObject(Map(
      "containers" -> Json.toJson(entities.values collect { case container: Container => container }),
      "creatures" -> Json.toJson(entities.values collect { case creature: Creature => creature }),
      "globals" -> Json.toJson(entities.values collect { case global: GlobalActor => global }),
      "items" -> Json.toJson(entities.values collect { case item: Item => item }),
      "machinery" -> Json.toJson(entities.values collect { case machinery: Machinery => machinery }),
      "statuses" -> Json.toJson(entities.values collect { case status: Status => status }),
      "terrains" -> Json.toJson(entities.values collect { case terrain: Terrain => terrain }),
      "walls" -> Json.toJson(entities.values collect { case wall: Wall => wall })
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value

      val list =
        obj("containers").as[List[Container]] :::
          obj("creatures").as[List[Creature]] :::
          obj("globals").as[List[GlobalActor]] :::
          obj("items").as[List[Item]] :::
          obj("machinery").as[List[Machinery]] :::
          obj("statuses").as[List[Status]] :::
          obj("terrains").as[List[Terrain]] :::
          obj("walls").as[List[Wall]]

      val map = list.map((x: Entity) => x.id -> x).toMap

      JsSuccess(map)
    }
  }
}
