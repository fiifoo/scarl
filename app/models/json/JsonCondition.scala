package models.json

import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.status._
import play.api.libs.json._

object JsonCondition {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val burningFormat = Json.format[Conditions.Burning]
  lazy private implicit val disorientedFormat = Json.format[Conditions.Disoriented]
  lazy private implicit val immobilizedFormat = Json.format[Conditions.Immobilized]

  lazy val conditionFormat: Format[Condition] = polymorphicTypeFormat(
    data => {
      case "Conditions.Burning" => data.as[Conditions.Burning]
      case "Conditions.Disoriented" => data.as[Conditions.Disoriented]
      case "Conditions.Immobilized" => data.as[Conditions.Immobilized]
    }, {
      case condition: Conditions.Burning => burningFormat.writes(condition)
      case condition: Conditions.Disoriented => disorientedFormat.writes(condition)
      case condition: Conditions.Immobilized => immobilizedFormat.writes(condition)
    }
  )
}
