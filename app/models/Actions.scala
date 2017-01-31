package models

import io.github.fiifoo.scarl.action.{AttackAction, MoveAction, PassAction}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import play.api.libs.json._

object Actions {

  object ActionType extends Enumeration {
    type ActionType = Value
    val Attack, Pass, Move = Value
  }

  import ActionType._

  implicit val actionTypeReads = Reads.enumNameReads(ActionType)

  implicit val creatureIdReads = Json.reads[CreatureId]
  implicit val locationReads = Json.reads[Location]

  implicit val attackActionReads = Json.reads[AttackAction]
  implicit val moveActionReads = Json.reads[MoveAction]

  implicit val actionReads = new Reads[Action] {
    def reads(json: JsValue): JsResult[Action] = {
      val container = json.as[JsObject].value
      val actionType = container("type").as[ActionType]
      val data = container("data")

      JsSuccess(actionType match {
        case Attack => data.as[AttackAction]
        case Pass => PassAction()
        case Move => data.as[MoveAction]
      })
    }
  }

  def fromJson(json: JsValue): Action = json.as[Action]
}
