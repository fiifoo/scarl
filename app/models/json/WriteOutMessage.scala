package models.json

import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.game.{LocationEntities, OutMessage, PlayerFov}
import models.json.FormatBase._
import models.json.FormatEntity._
import models.json.FormatGameState.{formatAreaMap, formatStatistics}
import models.json.FormatId._
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

  implicit val writeCreature = Json.writes[CreatureKind]
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

  implicit val writeOutMessage = Json.writes[OutMessage]
}
