package models.json

import io.github.fiifoo.scarl.game.api._
import play.api.libs.json._

object ReadInMessage {

  import JsonBase.polymorphicTypeReads

  def apply(json: JsValue): InMessage = inMessageReads.reads(json).get

  lazy private implicit val actionReads = JsonAction.actionReads
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat

  lazy private implicit val gameActionReads = Json.reads[GameAction]
  lazy private implicit val setEquipmentSetReads = Json.reads[SetEquipmentSet]
  lazy private implicit val setQuickItemReads = Json.reads[SetQuickItem]

  lazy private val inMessageReads: Reads[InMessage] = polymorphicTypeReads(data => {
    case "DebugFovQuery" => DebugFovQuery
    case "DebugWaypointQuery" => DebugWaypointQuery
    case "GameAction" => data.as[GameAction]
    case "InventoryQuery" => InventoryQuery
    case "SetEquipmentSet" => data.as[SetEquipmentSet]
    case "SetQuickItem" => data.as[SetQuickItem]
  })
}
