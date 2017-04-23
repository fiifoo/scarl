package models.json

import io.github.fiifoo.scarl.action.{AttackAction, CommunicateAction, MoveAction, PassAction}
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import models.json.FormatBase._
import models.json.FormatId._
import play.api.libs.json._

object ReadAction {

  def apply(json: JsValue): Action = {
    readAction.reads(json).get
  }

  implicit val readAttackAction = Json.reads[AttackAction]
  implicit val readCommunicateAction = new Reads[CommunicateAction] {
    def reads(json: JsValue): JsResult[CommunicateAction] = {
      val target = json.as[JsObject].value("target").as[CreatureId]

      JsSuccess(CommunicateAction(target))
    }
  }
  implicit val readMoveAction = Json.reads[MoveAction]

  implicit val readAction = new Reads[Action] {
    def reads(json: JsValue): JsResult[Action] = {
      val container = json.as[JsObject].value
      val actionType = container("type").as[String]
      val data = container("data")

      JsSuccess(actionType match {
        case "Attack" => data.as[AttackAction]
        case "Communicate" => data.as[CommunicateAction]
        case "Move" => data.as[MoveAction]
        case "Pass" => PassAction()
      })
    }
  }
}