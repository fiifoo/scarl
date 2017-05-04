package models.json

import io.github.fiifoo.scarl.core.character.Stats
import io.github.fiifoo.scarl.core.equipment._
import play.api.libs.json._

object FormatEquipment {

  private implicit val formatMeleeStats = Json.format[Stats.Melee]
  private implicit val formatRangedStats = Json.format[Stats.Ranged]
  private implicit val formatSightStats = Json.format[Stats.Sight]
  private implicit val formatStats = Json.format[Stats]

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
        case "RangedSlot" => RangedSlot
        case _ => formatArmorSlot.reads(json).get
      })
    }
  }

  implicit val formatArmor = Json.format[Armor]
  implicit val formatRangedWeapon = Json.format[RangedWeapon]
  implicit val formatShield = Json.format[Shield]
  implicit val formatWeapon = Json.format[Weapon]

  private def slotName(slot: Slot): String = {
    slot.getClass.getSimpleName.replace("$", "")
  }
}
