package models.json

import io.github.fiifoo.scarl.ai.strategy.RoamStrategy
import io.github.fiifoo.scarl.core.ai.Strategy
import play.api.libs.json._

object JsonStrategy {

  import JsonBase.polymorphicTypeFormat

  lazy implicit val strategyFormat: Format[Strategy] = polymorphicTypeFormat(
    _ => {
      case "RoamStrategy" => RoamStrategy
    }, {
      case RoamStrategy => JsNull
    }
  )
}
