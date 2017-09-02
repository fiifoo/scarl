package models.json

import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item.{Armor, RangedWeapon, Shield, Weapon}
import play.api.libs.json._

object JsonItemEquipment {

  import JsonBase.polymorphicObjectFormat

  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat

  lazy private implicit val armorSlotFormat = new Format[ArmorSlot] {
    def writes(o: ArmorSlot): JsValue = slotFormat.writes(o)

    def reads(json: JsValue): JsResult[ArmorSlot] = slotFormat.reads(json) map (_.asInstanceOf[ArmorSlot])
  }

  lazy implicit val slotFormat: Format[Slot] = polymorphicObjectFormat({
    case "MainHand" => MainHand
    case "OffHand" => OffHand
    case "RangedSlot" => RangedSlot
    case "HeadArmor" => HeadArmor
    case "ChestArmor" => ChestArmor
    case "HandArmor" => HandArmor
    case "LegArmor" => LegArmor
    case "FootArmor" => FootArmor
  })

  lazy implicit val armorFormat: Format[Armor] = Json.format[Armor]
  lazy implicit val rangedWeaponFormat: Format[RangedWeapon] = Json.format[RangedWeapon]
  lazy implicit val shieldFormat: Format[Shield] = Json.format[Shield]
  lazy implicit val weaponFormat: Format[Weapon] = Json.format[Weapon]
}
