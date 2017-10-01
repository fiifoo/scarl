package models.json

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.status._
import play.api.libs.json._

object JsonStatus {

  import JsonBase.{integerIdFormat, polymorphicIdFormat, polymorphicTypeFormat, weightedChoicesFormat}

  lazy private implicit val activeStatusIdFormat = integerIdFormat[ActiveStatusId](_.value, ActiveStatusId.apply)
  lazy private implicit val passiveStatusIdFormat = integerIdFormat[PassiveStatusId](_.value, PassiveStatusId.apply)
  lazy private implicit val triggerStatusIdFormat = integerIdFormat[TriggerStatusId](_.value, TriggerStatusId.apply)

  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val containerIdFormat = JsonContainer.containerIdFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  implicitly(weightedChoicesFormat[CreatureKindId])

  lazy private implicit val damagingTrapFormat = Json.format[DamagingTrapStatus]
  lazy private implicit val delayedTransformingWidgetFormat = Json.format[DelayedTransformingWidgetStatus]
  lazy private implicit val healLocationFormat = Json.format[HealLocationStatus]
  lazy private implicit val summonCreatureFormat = Json.format[SummonCreatureStatus]
  lazy private implicit val triggeredConduitFormat = Json.format[TriggeredConduitStatus]
  lazy private implicit val triggeredMachineryFormat = Json.format[TriggeredMachineryStatus]
  lazy private implicit val triggeredTransformingWidgetFormat = Json.format[TriggeredTransformingWidgetStatus]

  lazy val statusFormat: Format[Status] = polymorphicTypeFormat(
    data => {
      case "DamagingTrapStatus" => data.as[DamagingTrapStatus]
      case "DelayedTransformingWidgetStatus" => data.as[DelayedTransformingWidgetStatus]
      case "HealLocationStatus" => data.as[HealLocationStatus]
      case "SummonCreatureStatus" => data.as[SummonCreatureStatus]
      case "TriggeredConduitStatus" => data.as[TriggeredConduitStatus]
      case "TriggeredMachineryStatus" => data.as[TriggeredMachineryStatus]
      case "TriggeredTransformingWidgetStatus" => data.as[TriggeredTransformingWidgetStatus]
    }, {
      case status: DamagingTrapStatus => damagingTrapFormat.writes(status)
      case status: DelayedTransformingWidgetStatus => delayedTransformingWidgetFormat.writes(status)
      case status: HealLocationStatus => healLocationFormat.writes(status)
      case status: SummonCreatureStatus => summonCreatureFormat.writes(status)
      case status: TriggeredConduitStatus => triggeredConduitFormat.writes(status)
      case status: TriggeredMachineryStatus => triggeredMachineryFormat.writes(status)
      case status: TriggeredTransformingWidgetStatus => triggeredTransformingWidgetFormat.writes(status)
    }
  )

  lazy val statusIdFormat: Format[StatusId] = polymorphicIdFormat[StatusId, Int](
    value => {
      case "ActiveStatusId" => ActiveStatusId(value)
      case "PassiveStatusId" => PassiveStatusId(value)
      case "TriggerStatusId" => TriggerStatusId(value)
    }, {
      id => JsNumber(id.value)
    }
  )
}
