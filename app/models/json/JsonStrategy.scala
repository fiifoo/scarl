package models.json

import io.github.fiifoo.scarl.ai.strategy._
import io.github.fiifoo.scarl.core.ai.Strategy
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonStrategy {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val attackFormat = Json.format[AttackStrategy]

  lazy implicit val strategyFormat: Format[Strategy] = polymorphicTypeFormat(
    data => {
      case "RoamStrategy" => RoamStrategy

      case "AttackStrategy" =>
        data match {
          case o: JsObject if o.value.isEmpty => AttackStrategy()
          case _ => data.as[AttackStrategy]
        }
    }, {
      case RoamStrategy => JsNull

      case strategy: AttackStrategy => attackFormat.writes(strategy)
    }
  )
}
