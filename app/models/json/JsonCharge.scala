package models.json

import io.github.fiifoo.scarl.core.entity.Charge
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import play.api.libs.json._

object JsonCharge {
  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat

  lazy val chargeFormat = Json.format[Charge]
}
