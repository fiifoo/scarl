package models.json

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.kind.ItemKind.{Category, UtilityCategory}
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

  lazy implicit val categoryFormat: Format[Category] = polymorphicObjectFormat({
    case "ItemKind.UtilityCategory" => UtilityCategory
  })

  lazy val itemKindMapReads: Reads[Map[ItemKindId, ItemKind]] = mapReads
}
