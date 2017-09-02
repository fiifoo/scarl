package models.json

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.{Container, ContainerId}
import play.api.libs.json._

object JsonContainer {

  import JsonBase.integerIdFormat

  lazy private implicit val locationFormat = Json.format[Location]

  lazy implicit val containerIdFormat: Format[ContainerId] = integerIdFormat(_.value, ContainerId.apply)

  lazy val containerFormat: Format[Container] = Json.format[Container]
}
