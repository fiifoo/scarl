package models.json

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import models.json.FormatBase._
import models.json.FormatId._
import models.json.FormatItem.formatSlot
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
  implicit val readDropItemAction = Json.reads[DropItemAction]
  implicit val readEnterConduitAction = Json.reads[EnterConduitAction]
  implicit val readEquipItemAction = Json.reads[EquipItemAction]
  implicit val readMoveAction = Json.reads[MoveAction]
  implicit val readPickItemAction = Json.reads[PickItemAction]
  implicit val readShootAction = Json.reads[ShootAction]
  implicit val readShootMissileAction = Json.reads[ShootMissileAction]
  implicit val readUnequipItemAction = Json.reads[UnequipItemAction]
  implicit val readUseCreatureAction = Json.reads[UseCreatureAction]
  implicit val readUseDoorAction = Json.reads[UseDoorAction]
  implicit val readUseItemAction = Json.reads[UseItemAction]

  implicit val readAction = new Reads[Action] {
    def reads(json: JsValue): JsResult[Action] = {
      val container = json.as[JsObject].value
      val actionType = container("type").as[String]
      val data = container("data")

      JsSuccess(actionType match {
        case "Attack" => data.as[AttackAction]
        case "Communicate" => data.as[CommunicateAction]
        case "DropItem" => data.as[DropItemAction]
        case "EnterConduit" => data.as[EnterConduitAction]
        case "EquipItem" => data.as[EquipItemAction]
        case "Move" => data.as[MoveAction]
        case "Pass" => PassAction()
        case "PickItem" => data.as[PickItemAction]
        case "Shoot" => data.as[ShootAction]
        case "ShootMissile" => data.as[ShootMissileAction]
        case "UnequipItem" => data.as[UnequipItemAction]
        case "UseCreature" => data.as[UseCreatureAction]
        case "UseDoor" => data.as[UseDoorAction]
        case "UseItem" => data.as[UseItemAction]
      })
    }
  }
}
