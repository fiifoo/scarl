package models.json

import io.github.fiifoo.scarl.core.item.Door
import play.api.libs.json._

object JsonItemDoor {

  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat

  lazy val doorFormat: Format[Door] = Json.format[Door]
}
