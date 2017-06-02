package models.json

import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.game.api.OutMessage.PlayerInfo
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.event._
import io.github.fiifoo.scarl.game.{LocationEntities, PlayerFov}
import models.json.FormatBase._
import models.json.FormatEntity._
import models.json.FormatGameState.{formatAreaMap, formatStatistics}
import models.json.FormatId._
import models.json.FormatItem._
import models.json.FormatUtils._
import play.api.libs.json._

object WriteOutMessage {

  def apply(message: OutMessage): JsValue = {
    writeOutMessage.writes(message)
  }

  implicit val formatLocationEntities = Json.format[LocationEntities]
  implicit val formatLocationEntitiesMap = formatMap(formatLocation, formatLocationEntities)
  implicit val writePlayerFov = new Writes[PlayerFov] {
    def writes(fov: PlayerFov): JsValue = JsObject(Map(
      "delta" -> Json.toJson(fov.delta),
      "shouldHide" -> Json.toJson(fov.shouldHide)
    ))
  }

  implicit val writeEmptyCommunications = new Writes[Map[FactionId, List[CommunicationId]]] {
    def writes(m: Map[FactionId, List[CommunicationId]]): JsValue = JsNull
  }

  implicit val writeCreatureKind = Json.writes[CreatureKind]
  implicit val writeItemKind = Json.writes[ItemKind]
  implicit val writeTerrainKind = Json.writes[TerrainKind]
  implicit val writeWallKind = Json.writes[WallKind]
  implicit val writeKinds = new Writes[Kinds] {
    def writes(k: Kinds) = JsObject(Map(
      "creatures" -> Json.toJson(k.creatures.values),
      "items" -> Json.toJson(k.items.values),
      "terrains" -> Json.toJson(k.terrains.values),
      "walls" -> Json.toJson(k.walls.values)
    ))
  }

  // writes have conflict with EntityIds for some reason
  implicit val formatExplosionEvent = Json.format[ExplosionEvent]
  implicit val formatGenericEvent = Json.format[GenericEvent]
  implicit val formatHitEvent = Json.format[HitEvent]
  implicit val formatMoveMissileEvent = Json.format[MoveMissileEvent]
  implicit val formatShotEvent = Json.format[ShotEvent]

  implicit val writeEvent = new Writes[Event] {
    def writes(event: Event): JsValue = {
      val data = event match {
        case event: ExplosionEvent => formatExplosionEvent.writes(event)
        case event: GenericEvent => formatGenericEvent.writes(event)
        case event: HitEvent => formatHitEvent.writes(event)
        case event: MoveMissileEvent => formatMoveMissileEvent.writes(event)
        case event: ShotEvent => formatShotEvent.writes(event)
      }

      JsObject(Map(
        "type" -> JsString(event.getClass.getSimpleName),
        "data" -> data
      ))
    }
  }

  implicit val formatFaction = Json.format[Faction]
  implicit val formatEquipments = formatMap(formatSlot, formatItemId)
  implicit val writePlayerInfo = Json.writes[PlayerInfo]

  implicit val writeGameStart = Json.writes[GameStart]
  implicit val writeGameUpdate = Json.writes[GameUpdate]
  implicit val writeGameOver = Json.writes[GameOver]
  implicit val writeAreaChange = Json.writes[AreaChange]
  implicit val writePlayerInventory = Json.writes[PlayerInventory]

  implicit val writeOutMessage = new Writes[OutMessage] {
    def writes(message: OutMessage): JsValue = {
      val data = message match {
        case message: GameStart => writeGameStart.writes(message)
        case message: GameUpdate => writeGameUpdate.writes(message)
        case message: GameOver => writeGameOver.writes(message)
        case message: AreaChange => writeAreaChange.writes(message)
        case message: PlayerInventory => writePlayerInventory.writes(message)
      }

      JsObject(Map(
        "type" -> JsString(message.getClass.getSimpleName),
        "data" -> data
      ))
    }
  }
}
