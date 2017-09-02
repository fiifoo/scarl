package models.json

import io.github.fiifoo.scarl.core.kind.{CreatureKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.widget._
import play.api.libs.json._

object JsonWidgetKind {

  import JsonBase.{mapReads, polymorphicTypeReads, stringIdFormat, weightedChoicesReads}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat

  implicitly(weightedChoicesReads[CreatureKindId])

  lazy private implicit val damagingTrapReads = Json.reads[DamagingTrapWidget]
  lazy private implicit val delayedTransformingReads = Json.reads[DelayedTransformingWidget]
  lazy private implicit val healLocationReads = Json.reads[HealLocationWidget]
  lazy private implicit val summonCreatureReads = Json.reads[SummonCreatureWidget]
  lazy private implicit val triggeredTransformingReads = Json.reads[TriggeredTransformingWidget]

  lazy implicit val widgetKindIdFormat: Format[WidgetKindId] = stringIdFormat(_.value, WidgetKindId.apply)

  lazy implicit val widgetKindReads: Reads[WidgetKind] = polymorphicTypeReads(data => {
    case "DamagingTrapWidget" => data.as[DamagingTrapWidget]
    case "DelayedTransformingWidget" => data.as[DelayedTransformingWidget]
    case "HealLocationWidget" => data.as[HealLocationWidget]
    case "SummonCreatureWidget" => data.as[SummonCreatureWidget]
    case "TriggeredTransformingWidget" => data.as[TriggeredTransformingWidget]
  })

  lazy val widgetKindMapReads: Reads[Map[WidgetKindId, WidgetKind]] = mapReads
}
