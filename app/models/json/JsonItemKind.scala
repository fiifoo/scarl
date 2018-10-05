package models.json

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.ItemKind.{Category, DoorCategory}
import io.github.fiifoo.scarl.core.kind.{ItemKind, ItemKindId}
import play.api.libs.json._

object JsonItemKind {

  import JsonBase.{mapReads, polymorphicObjectFormat, stringIdFormat}

  lazy private implicit val armorFormat = JsonItemEquipment.armorFormat
  lazy private implicit val charFormat = JsonBase.charFormat
  lazy private implicit val doorFormat = JsonItemDoor.doorFormat
  lazy private implicit val explosiveFormat = Json.format[Explosive]
  lazy private implicit val itemPowerFormat = JsonPower.itemPowerFormat
  lazy private implicit val lockSourceFormat = JsonItemLock.lockSourceFormat
  lazy private implicit val missileLauncherFormat = JsonItemEquipment.missileLauncherFormat
  lazy private implicit val rangedWeaponFormat = JsonItemEquipment.rangedWeaponFormat
  lazy private implicit val sharedKeyFormat = JsonItemKey.sharedKeyFormat
  lazy private implicit val shieldFormat = JsonItemEquipment.shieldFormat
  lazy private implicit val weaponFormat = JsonItemEquipment.weaponFormat

  lazy implicit val itemKindIdFormat: Format[ItemKindId] = stringIdFormat(_.value, ItemKindId.apply)

  lazy implicit val itemKindFormat: Format[ItemKind] = Json.format

  lazy val itemKindMapReads: Reads[Map[ItemKindId, ItemKind]] = mapReads

  lazy val itemCategoryFormat: Format[Category] = polymorphicObjectFormat({
    case "ItemKind.UtilityCategory" => ItemKind.UtilityCategory

    case "ItemKind.DefaultDoorCategory" => ItemKind.DefaultDoorCategory
    case "ItemKind.SecureDoorCategory" => ItemKind.SecureDoorCategory

    case "Equipment.HeadArmorCategory" => Equipment.HeadArmorCategory
    case "Equipment.BodyArmorCategory" => Equipment.BodyArmorCategory
    case "Equipment.HandArmorCategory" => Equipment.HandArmorCategory
    case "Equipment.FootArmorCategory" => Equipment.FootArmorCategory
    case "Equipment.LauncherCategory" => Equipment.LauncherCategory
    case "Equipment.RangedWeaponCategory" => Equipment.RangedWeaponCategory
    case "Equipment.ShieldCategory" => Equipment.ShieldCategory
    case "Equipment.WeaponCategory" => Equipment.WeaponCategory
  })

  lazy val doorCategoryFormat: Format[DoorCategory] = polymorphicObjectFormat({
    case "ItemKind.DefaultDoorCategory" => ItemKind.DefaultDoorCategory
    case "ItemKind.SecureDoorCategory" => ItemKind.SecureDoorCategory
  })
}
