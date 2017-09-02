package models.json

import io.github.fiifoo.scarl.core.kind.{ItemKind, ItemKindId}
import play.api.libs.json._

object JsonItemKind {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val armorFormat = JsonItemEquipment.armorFormat
  lazy private implicit val charFormat = JsonBase.charFormat
  lazy private implicit val doorFormat = JsonItemDoor.doorFormat
  lazy private implicit val itemPowerIdFormat = JsonItemPower.itemPowerIdFormat
  lazy private implicit val rangedWeaponFormat = JsonItemEquipment.rangedWeaponFormat
  lazy private implicit val shieldFormat = JsonItemEquipment.shieldFormat
  lazy private implicit val weaponFormat = JsonItemEquipment.weaponFormat

  lazy implicit val itemKindIdFormat: Format[ItemKindId] = stringIdFormat(_.value, ItemKindId.apply)

  lazy implicit val itemKindFormat: Format[ItemKind] = Json.format

  lazy val itemKindMapReads: Reads[Map[ItemKindId, ItemKind]] = mapReads
}
