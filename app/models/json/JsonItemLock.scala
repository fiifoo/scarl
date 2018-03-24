package models.json

import io.github.fiifoo.scarl.core.item.Lock
import play.api.libs.json._

object JsonItemLock {

  lazy private implicit val keyFormat = JsonItemKey.keyFormat
  lazy private implicit val sharedKeyFormat = JsonItemKey.sharedKeyFormat

  lazy val lockFormat: Format[Lock] = Json.format[Lock]
  lazy val lockSourceFormat: Format[Lock.Source] = Json.format[Lock.Source]
}
