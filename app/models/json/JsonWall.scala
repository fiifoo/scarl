package models.json

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonWall {

  import JsonBase.integerIdFormat

  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat

  lazy implicit val wallIdFormat: Format[WallId] = integerIdFormat(_.value, WallId.apply)

  lazy val wallFormat: Format[Wall] = Json.format[Wall]
}
