package models.json

import io.github.fiifoo.scarl.core.power.{ItemPower, ItemPowerId}
import io.github.fiifoo.scarl.power.TransformItemPower
import play.api.libs.json._

object JsonItemPower {

  import JsonBase.{mapReads, polymorphicTypeReads, stringIdFormat}

  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat
  lazy private implicit val transformItem = Json.reads[TransformItemPower]

  lazy implicit val itemPowerIdFormat: Format[ItemPowerId] = stringIdFormat(_.value, ItemPowerId.apply)

  lazy implicit val itemPowerReads: Reads[ItemPower] = polymorphicTypeReads(data => {
    case "TransformItemPower" => data.as[TransformItemPower]
  })

  lazy val itemPowerMapReads: Reads[Map[ItemPowerId, ItemPower]] = mapReads
}
