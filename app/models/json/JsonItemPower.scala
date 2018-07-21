package models.json

import io.github.fiifoo.scarl.core.item.ItemPower
import io.github.fiifoo.scarl.power._
import play.api.libs.json._

object JsonItemPower {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat

  lazy private implicit val removeItemFormat = Json.format[RemoveItemPower]
  lazy private implicit val transformItemFormat = Json.format[TransformItemPower]
  lazy private implicit val trapAttackFormat = Json.format[TrapAttackPower]

  lazy implicit val itemPowerFormat: Format[ItemPower] = polymorphicTypeFormat(
    data => {
      case "ExplodeItemPower" => ExplodeItemPower
      case "ReceiveKeyPower" => ReceiveKeyPower
      case "RemoveItemPower" => data.as[RemoveItemPower]
      case "TransformItemPower" => data.as[TransformItemPower]
      case "TrapAttackPower" => data.as[TrapAttackPower]
    }, {
      case ExplodeItemPower => JsNull
      case ReceiveKeyPower => JsNull
      case power: RemoveItemPower => removeItemFormat.writes(power)
      case power: TransformItemPower => transformItemFormat.writes(power)
      case power: TrapAttackPower => trapAttackFormat.writes(power)
    }
  )
}
