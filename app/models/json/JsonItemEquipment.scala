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
    case "HeadArmorCategory" => HeadArmorCategory
    case "BodyArmorCategory" => BodyArmorCategory
    case "HandArmorCategory" => HandArmorCategory
    case "FootArmorCategory" => FootArmorCategory
    case "LauncherCategory" => LauncherCategory
    case "RangedWeaponCategory" => RangedWeaponCategory
    case "ShieldCategory" => ShieldCategory
    case "WeaponCategory" => WeaponCategory
  })

  lazy implicit val slotFormat: Format[Slot] = polymorphicObjectFormat({
    case "MainHand" => MainHand
    case "OffHand" => OffHand
    case "RangedSlot" => RangedSlot
    case "LauncherSlot" => LauncherSlot
    case "HeadArmor" => HeadArmor
    case "BodyArmor" => BodyArmor
    case "HandArmor" => HandArmor
    case "FootArmor" => FootArmor
  })

  lazy implicit val armorFormat: Format[Armor] = Json.format[Armor]
  lazy implicit val missileLauncherFormat: Format[MissileLauncher] = Json.format[MissileLauncher]
  lazy implicit val rangedWeaponFormat: Format[RangedWeapon] = Json.format[RangedWeapon]
  lazy implicit val shieldFormat: Format[Shield] = Json.format[Shield]
  lazy implicit val weaponFormat: Format[Weapon] = Json.format[Weapon]
}
