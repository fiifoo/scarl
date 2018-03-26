package models.json

import io.github.fiifoo.scarl.core.kind.WidgetKind.{Category, HealCategory, PortalCategory, TrapCategory}
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.widget._
import play.api.libs.json._

object JsonWidgetKind {

  import JsonBase.{mapReads, polymorphicObjectFormat, polymorphicTypeReads, stringIdFormat, weightedChoicesReads}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat

  implicitly(weightedChoicesReads[CreatureKindId])
  lazy private implicit val discoverFormat = JsonItemDiscover.discoverFormat

  lazy private implicit val delayedTransformingReads = Json.reads[DelayedTransformingWidget]
  lazy private implicit val healLocationReads = Json.reads[HealLocationWidget]
  lazy private implicit val summonCreatureReads = Json.reads[SummonCreatureWidget]
  lazy private implicit val timedExplosiveWidgetReads = Json.reads[TimedExplosiveWidget]
  lazy private implicit val triggeredMachineryWidgetReads = Json.reads[TriggeredMachineryWidget]
  lazy private implicit val triggeredTransformingReads = Json.reads[TriggeredTransformingWidget]
  lazy private implicit val triggeredTrapReads = Json.reads[TriggeredTrapWidget]

  lazy implicit val widgetKindIdFormat: Format[WidgetKindId] = stringIdFormat(_.value, WidgetKindId.apply)

  lazy implicit val widgetKindReads: Reads[WidgetKind] = polymorphicTypeReads(data => {
    case "DelayedTransformingWidget" => data.as[DelayedTransformingWidget]
    case "HealLocationWidget" => data.as[HealLocationWidget]
    case "SummonCreatureWidget" => data.as[SummonCreatureWidget]
    case "TimedExplosiveWidget" => data.as[TimedExplosiveWidget]
    case "TriggeredMachineryWidget" => data.as[TriggeredMachineryWidget]
    case "TriggeredTransformingWidget" => data.as[TriggeredTransformingWidget]
    case "TriggeredTrapWidget" => data.as[TriggeredTrapWidget]
  })

  lazy implicit val categoryFormat: Format[Category] = polymorphicObjectFormat({
    case "WidgetKind.HealCategory" => HealCategory
    case "WidgetKind.PortalCategory" => PortalCategory
    case "WidgetKind.TrapCategory" => TrapCategory
  })

  lazy val widgetKindMapReads: Reads[Map[WidgetKindId, WidgetKind]] = mapReads
}
