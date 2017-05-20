package models.json

import io.github.fiifoo.scarl.game.api.{GameAction, InMessage, InventoryQuery}
import models.json.FormatBase._
import models.json.FormatEquipment.formatSlot
import models.json.FormatId._
import models.json.ReadAction.readAction
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
        case "GameAction" => data.as[GameAction]
        case "InventoryQuery" => InventoryQuery()
      })
    }
  }
}
