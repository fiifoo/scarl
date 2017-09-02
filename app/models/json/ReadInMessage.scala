package models.json

import io.github.fiifoo.scarl.game.api._
import play.api.libs.json._

object ReadInMessage {

  import JsonBase.polymorphicTypeReads

  def apply(json: JsValue): InMessage = inMessageReads.reads(json).get

  lazy private implicit val actionReads = JsonAction.actionReads
  lazy private implicit val gameActionReads = Json.reads[GameAction]

  lazy private val inMessageReads: Reads[InMessage] = polymorphicTypeReads(data => {
    case "DebugFovQuery" => DebugFovQuery
    case "DebugWaypointQuery" => DebugWaypointQuery
    case "GameAction" => data.as[GameAction]
    case "InventoryQuery" => InventoryQuery
  })
}
