package models.json

import io.github.fiifoo.scarl.core.kind.{WallKind, WallKindId}
import play.api.libs.json._

object JsonWallKind {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val charFormat = JsonBase.charFormat

  lazy implicit val wallKindIdFormat: Format[WallKindId] = stringIdFormat(_.value, WallKindId.apply)

  lazy implicit val wallKindFormat: Format[WallKind] = Json.format

  lazy val wallKindMapReads: Reads[Map[WallKindId, WallKind]] = mapReads
}
