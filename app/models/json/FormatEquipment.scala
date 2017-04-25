package models.json

import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.equipment._
import play.api.libs.json._

object FormatEquipment {

  private implicit val formatCreatureSight = Json.format[Creature.Sight]
  private implicit val formatCreatureStats = Json.format[Creature.Stats]

  implicit val formatArmorSlot = new Format[ArmorSlot] {
    def writes(slot: ArmorSlot): JsValue = {
      JsString(slotName(slot))
    }

    def reads(json: JsValue): JsResult[ArmorSlot] = {
      val slot = json.as[String]

      JsSuccess(slot match {
        case "HeadArmor" => HeadArmor
        case "ChestArmor" => ChestArmor
        case "HandArmor" => HandArmor
        case "LegArmor" => LegArmor
        case "FootArmor" => FootArmor
      })
    }
  }

  implicit val formatSlot = new Format[Slot] {
    def writes(slot: Slot): JsValue = {
      JsString(slotName(slot))
    }

    def reads(json: JsValue): JsResult[Slot] = {
      val slot = json.as[String]

      JsSuccess(slot match {
        case "MainHand" => MainHand
        case "OffHand" => OffHand
        case _ => formatArmorSlot.reads(json).get
      })
    }
  }

  implicit val formatArmor = Json.format[Armor]
  implicit val formatShield = Json.format[Shield]
  implicit val formatWeapon = Json.format[Weapon]

  private def slotName(slot: Slot): String = {
    slot.getClass.getSimpleName.replace("$", "")
  }
}
