package models.json

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonMachinery {

  import JsonBase.integerIdFormat

  lazy private implicit val locationFormat = Json.format[Location]

  private lazy implicit val mechanismFormat = JsonMechanism.mechanismFormat

  lazy implicit val machineryIdFormat: Format[MachineryId] = integerIdFormat(_.value, MachineryId.apply)

  lazy val machineryFormat: Format[Machinery] = Json.format[Machinery]
}
