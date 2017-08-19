package game.json

import game.json.FormatBase._
import game.json.FormatId._
import game.json.FormatItem.formatSlot
import game.json.ReadAction.readAction
import io.github.fiifoo.scarl.game.api._
import play.api.libs.json._

object ReadInMessage {

  def apply(json: JsValue): InMessage = {
    readInMessage.reads(json).get
  }

  implicit val readGameAction = Json.reads[GameAction]

  implicit val readInMessage = new Reads[InMessage] {
    def reads(json: JsValue): JsResult[InMessage] = {
      val container = json.as[JsObject].value
      val messageType = container("type").as[String]
      val data = container("data")

      JsSuccess(messageType match {
        case "DebugFovQuery" => DebugFovQuery
        case "DebugWaypointQuery" => DebugWaypointQuery
        case "GameAction" => data.as[GameAction]
        case "InventoryQuery" => InventoryQuery
      })
    }
  }
}
