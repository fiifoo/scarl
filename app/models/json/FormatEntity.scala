package models.json

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.status._
import models.json.FormatBase._
import models.json.FormatId._
import models.json.FormatUtils._
import play.api.libs.json._

object FormatEntity {
  implicit val formatCreatureKindChoice = formatWeightedChoices(formatCreatureKindId)
  implicit val formatCreatureSight = Json.format[Creature.Sight]
  implicit val formatCreatureStats = Json.format[Creature.Stats]

  implicit val formatContainer = Json.format[Container]
  implicit val formatCreature = Json.format[Creature]
  implicit val formatItem = Json.format[Item]
  implicit val formatTerrain = Json.format[Terrain]
  implicit val formatWall = Json.format[Wall]

  implicit val formatDeathStatus = Json.format[DeathStatus]
  implicit val formatDelayedTransformingWidgetStatus = Json.format[DelayedTransformingWidgetStatus]
  implicit val formatHealLocationStatus = Json.format[HealLocationStatus]
  implicit val formatSummonCreatureStatus = Json.format[SummonCreatureStatus]
  implicit val formatTriggeredConduitStatus = Json.format[TriggeredConduitStatus]
  implicit val formatTriggeredTransformingWidgetStatus = Json.format[TriggeredTransformingWidgetStatus]

  implicit val formatStatus = new Format[Status] {
    def writes(status: Status) = JsObject(Map(
      "type" -> JsString(status.getClass.getSimpleName),
      "value" -> (status match {
        case status: DeathStatus => Json.toJson(status)
        case status: DelayedTransformingWidgetStatus => Json.toJson(status)
        case status: HealLocationStatus => Json.toJson(status)
        case status: SummonCreatureStatus => Json.toJson(status)
        case status: TriggeredConduitStatus => Json.toJson(status)
        case status: TriggeredTransformingWidgetStatus => Json.toJson(status)
      })
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value
      val value = obj("value")

      val status = obj("type").as[String] match {
        case "DeathStatus" => value.as[DeathStatus]
        case "DelayedTransformingWidgetStatus" => value.as[DelayedTransformingWidgetStatus]
        case "HealLocationStatus" => value.as[HealLocationStatus]
        case "SummonCreatureStatus" => value.as[SummonCreatureStatus]
        case "TriggeredConduitStatus" => value.as[TriggeredConduitStatus]
        case "TriggeredTransformingWidgetStatus" => value.as[TriggeredTransformingWidgetStatus]
      }

      JsSuccess(status)
    }
  }
}
