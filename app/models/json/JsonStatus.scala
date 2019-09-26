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

  lazy private implicit val chargeFormat = JsonCharge.chargeFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val conditionFormat = JsonCondition.conditionFormat
  lazy private implicit val containerIdFormat = JsonContainer.containerIdFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val stanceFormat = JsonStance.stanceFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  implicitly(weightedChoicesFormat[CreatureKindId])
  lazy private implicit val discoverFormat = JsonItemDiscover.discoverFormat

  lazy private implicit val creatureConditionFormat = Json.format[CreatureConditionStatus]
  lazy private implicit val creatureStanceFormat = Json.format[CreatureStanceStatus]
  lazy private implicit val delayedTransformingWidgetFormat = Json.format[DelayedTransformingWidgetStatus]
  lazy private implicit val healLocationFormat = Json.format[HealLocationStatus]
  lazy private implicit val summonCreatureFormat = Json.format[SummonCreatureStatus]
  lazy private implicit val timedExplosiveFormat = Json.format[TimedExplosiveStatus]
  lazy private implicit val triggeredConduitFormat = Json.format[TriggeredConduitStatus]
  lazy private implicit val triggeredTransformingWidgetFormat = Json.format[TriggeredTransformingWidgetStatus]
  lazy private implicit val triggeredTrapFormat = Json.format[TriggeredTrapStatus]

  lazy val statusFormat: Format[Status] = polymorphicTypeFormat(
    data => {
      case "CreatureConditionStatus" => data.as[CreatureConditionStatus]
      case "CreatureStanceStatus" => data.as[CreatureStanceStatus]
      case "DelayedTransformingWidgetStatus" => data.as[DelayedTransformingWidgetStatus]
      case "HealLocationStatus" => data.as[HealLocationStatus]
      case "SummonCreatureStatus" => data.as[SummonCreatureStatus]
      case "TimedExplosiveStatus" => data.as[TimedExplosiveStatus]
      case "TriggeredConduitStatus" => data.as[TriggeredConduitStatus]
      case "TriggeredTransformingWidgetStatus" => data.as[TriggeredTransformingWidgetStatus]
      case "TriggeredTrapStatus" => data.as[TriggeredTrapStatus]
    }, {
      case status: CreatureConditionStatus => creatureConditionFormat.writes(status)
      case status: CreatureStanceStatus => creatureStanceFormat.writes(status)
      case status: DelayedTransformingWidgetStatus => delayedTransformingWidgetFormat.writes(status)
      case status: HealLocationStatus => healLocationFormat.writes(status)
      case status: SummonCreatureStatus => summonCreatureFormat.writes(status)
      case status: TimedExplosiveStatus => timedExplosiveFormat.writes(status)
      case status: TriggeredConduitStatus => triggeredConduitFormat.writes(status)
      case status: TriggeredTransformingWidgetStatus => triggeredTransformingWidgetFormat.writes(status)
      case status: TriggeredTrapStatus => triggeredTrapFormat.writes(status)
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
