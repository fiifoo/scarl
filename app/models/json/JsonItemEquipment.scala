package models.json

import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item._
import play.api.libs.json._

object JsonItemEquipment {

  import JsonBase.polymorphicObjectFormat

  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat

  lazy private implicit val armorSlotFormat = new Format[ArmorSlot] {
    def writes(o: ArmorSlot): JsValue = slotFormat.writes(o)

    def reads(json: JsValue): JsResult[ArmorSlot] = slotFormat.reads(json) map (_.asInstanceOf[ArmorSlot])
  }

  lazy implicit val categoryFormat: Format[Category] = polymorphicObjectFormat({
    case "Equipment.HeadArmorCategory" => HeadArmorCategory
    case "Equipment.BodyArmorCategory" => BodyArmorCategory
    case "Equipment.HandArmorCategory" => HandArmorCategory
    case "Equipment.FootArmorCategory" => FootArmorCategory
    case "Equipment.LauncherCategory" => LauncherCategory
    case "Equipment.RangedWeaponCategory" => RangedWeaponCategory
    case "Equipment.ShieldCategory" => ShieldCategory
    case "Equipment.WeaponCategory" => WeaponCategory
  })

  lazy implicit val slotFormat: Format[Slot] = polymorphicObjectFormat({
    case "Equipment.MainHand" => MainHand
    case "Equipment.OffHand" => OffHand
    case "Equipment.RangedSlot" => RangedSlot
    case "Equipment.LauncherSlot" => LauncherSlot
    case "Equipment.HeadArmor" => HeadArmor
    case "Equipment.BodyArmor" => BodyArmor
    case "Equipment.HandArmor" => HandArmor
    case "Equipment.FootArmor" => FootArmor
  })

  lazy implicit val armorFormat: Format[Armor] = Json.format[Armor]
  lazy implicit val missileLauncherFormat: Format[MissileLauncher] = Json.format[MissileLauncher]
  lazy implicit val rangedWeaponFormat: Format[RangedWeapon] = Json.format[RangedWeapon]
  lazy implicit val shieldFormat: Format[Shield] = Json.format[Shield]
  lazy implicit val weaponFormat: Format[Weapon] = Json.format[Weapon]
}
