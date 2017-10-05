package models.json

import io.github.fiifoo.scarl.core.power.{CreaturePower, CreaturePowerId}
import io.github.fiifoo.scarl.power.TransformCreaturePower
import play.api.libs.json._

object JsonCreaturePower {

  import JsonBase.{mapReads, polymorphicTypeReads, stringIdFormat}

  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat
  lazy private implicit val transformCreature = Json.reads[TransformCreaturePower]

  lazy implicit val creaturePowerIdFormat: Format[CreaturePowerId] = stringIdFormat(_.value, CreaturePowerId.apply)

  lazy implicit val creaturePowerReads: Reads[CreaturePower] = polymorphicTypeReads(data => {
    case "TransformCreaturePower" => data.as[TransformCreaturePower]
  })

  lazy val creaturePowerMapReads: Reads[Map[CreaturePowerId, CreaturePower]] = mapReads
}
