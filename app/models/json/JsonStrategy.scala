package models.json

import io.github.fiifoo.scarl.ai.strategy.HuntStrategy
import io.github.fiifoo.scarl.core.ai.Strategy
import play.api.libs.json._

object JsonStrategy {

  import JsonBase.polymorphicTypeFormat

  lazy implicit val strategyFormat: Format[Strategy] = polymorphicTypeFormat(
    _ => {
      case "HuntStrategy" => HuntStrategy
    }, {
      case HuntStrategy => JsNull
    }
  )
}
