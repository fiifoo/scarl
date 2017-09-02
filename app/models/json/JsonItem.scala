package models.json

import io.github.fiifoo.scarl.core.entity._
import play.api.libs.json._

object JsonItem {

  import JsonBase.integerIdFormat

  lazy private implicit val armorFormat = JsonItemEquipment.armorFormat
  lazy private implicit val doorFormat = JsonItemDoor.doorFormat
  lazy private implicit val entityIdFormat = JsonEntity.entityIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemPowerIdFormat = JsonItemPower.itemPowerIdFormat
  lazy private implicit val rangedWeaponFormat = JsonItemEquipment.rangedWeaponFormat
  lazy private implicit val shieldFormat = JsonItemEquipment.shieldFormat
  lazy private implicit val weaponFormat = JsonItemEquipment.weaponFormat

  lazy implicit val itemIdFormat: Format[ItemId] = integerIdFormat(_.value, ItemId.apply)

  lazy val itemFormat: Format[Item] = Json.format[Item]
}
