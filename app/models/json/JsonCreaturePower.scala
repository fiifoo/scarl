package models.json

import io.github.fiifoo.scarl.core.entity.CreaturePower
import io.github.fiifoo.scarl.power.TransformCreaturePower
import play.api.libs.json._

object JsonCreaturePower {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat
  lazy private implicit val transformCreatureFormat = Json.format[TransformCreaturePower]

  lazy implicit val creaturePowerFormat: Format[CreaturePower] = polymorphicTypeFormat(
    data => {
      case "TransformCreaturePower" => data.as[TransformCreaturePower]
    }, {
      case power: TransformCreaturePower => transformCreatureFormat.writes(power)
    }
  )
}
