package models.json

import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.game.player.Settings
import play.api.libs.json._

object JsonPlayerSettings {

  import JsonBase.mapFormat

  lazy private implicit val itemIdFormat = JsonItem.itemIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val slotFormat = JsonItemEquipment.slotFormat

  implicitly(mapFormat[Int, ItemKindId])

  lazy val playerSettingsFormat: Format[Settings] = Json.format[Settings]
}
