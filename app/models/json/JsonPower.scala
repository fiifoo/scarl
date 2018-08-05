package models.json

import io.github.fiifoo.scarl.core.entity.{CreaturePower, ItemPower}
import io.github.fiifoo.scarl.power._
import play.api.libs.json._

object JsonPower {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat

  lazy private implicit val activateMachineryFormat = Json.format[ActivateMachineryPower]
  lazy private implicit val explodeItemFormat = Json.format[ExplodeItemPower]
  lazy private implicit val receiveKeyFormat = Json.format[ReceiveKeyPower]
  lazy private implicit val removeItemFormat = Json.format[RemoveItemPower]
  lazy private implicit val transformFormat = Json.format[TransformPower]
  lazy private implicit val trapAttackFormat = Json.format[TrapAttackPower]

  lazy implicit val creaturePowerFormat: Format[CreaturePower] = polymorphicTypeFormat(
    data => {
      case "TransformPower" => data.as[TransformPower]
    }, {
      case power: TransformPower => transformFormat.writes(power)
    }
  )

  lazy implicit val itemPowerFormat: Format[ItemPower] = polymorphicTypeFormat(
    data => {
      case "ActivateMachineryPower" => data.as[ActivateMachineryPower]
      case "ExplodeItemPower" => data.as[ExplodeItemPower]
      case "ReceiveKeyPower" => data.as[ReceiveKeyPower]
      case "RemoveItemPower" => data.as[RemoveItemPower]
      case "TransformPower" => data.as[TransformPower]
      case "TrapAttackPower" => data.as[TrapAttackPower]
    }, {
      case power: ActivateMachineryPower => activateMachineryFormat.writes(power)
      case power: ExplodeItemPower => explodeItemFormat.writes(power)
      case power: ReceiveKeyPower => receiveKeyFormat.writes(power)
      case power: RemoveItemPower => removeItemFormat.writes(power)
      case power: TransformPower => transformFormat.writes(power)
      case power: TrapAttackPower => trapAttackFormat.writes(power)
    }
  )
}
