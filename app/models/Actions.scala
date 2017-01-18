package models

import io.github.fiifoo.scarl.action.{MoveAction, PassAction}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.action.Action
import play.api.libs.json._

object Actions {

  object ActionType extends Enumeration {
    type ActionType = Value
    val Pass, Move = Value
  }

  import ActionType._

  implicit val actionTypeReads = Reads.enumNameReads(ActionType)
  implicit val locationReads = Json.reads[Location]
  implicit val moveActionReads = Json.reads[MoveAction]

  implicit val actionReads = new Reads[Action] {
    def reads(json: JsValue): JsResult[Action] = {
      val container = json.as[JsObject].value
      val actionType = container("type").as[ActionType]
      val data = container("data")

      JsSuccess(actionType match {
        case Pass => PassAction()
        case Move => data.as[MoveAction]
      })
    }
  }

  def fromClient(json: JsValue): Action = json.as[Action]
}
