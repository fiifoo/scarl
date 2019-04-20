package models.json

import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind.{WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.widget._
import play.api.libs.json._

object JsonWidgetKind {

  import JsonBase.{mapReads, polymorphicObjectFormat, polymorphicTypeFormat, stringIdFormat}

  lazy private implicit val chargeFormat = JsonCharge.chargeFormat
  lazy private implicit val creatureCatalogueIdFormat = JsonCatalogues.creatureCatalogueIdFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val creatureCategoryFormat = JsonCreatureKind.creatureCategoryFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat

  lazy private implicit val discoverFormat = JsonItemDiscover.discoverFormat

  lazy private implicit val delayedTransformingFormat = Json.format[DelayedTransformingWidget]
  lazy private implicit val healLocationFormat = Json.format[HealLocationWidget]
  lazy private implicit val summonCreatureFormat = Json.format[SummonCreatureWidget]
  lazy private implicit val timedExplosiveWidgetFormat = Json.format[TimedExplosiveWidget]
  lazy private implicit val triggeredTransformingFormat = Json.format[TriggeredTransformingWidget]
  lazy private implicit val triggeredTrapFormat = Json.format[TriggeredTrapWidget]

  lazy implicit val widgetKindIdFormat: Format[WidgetKindId] = stringIdFormat(_.value, WidgetKindId.apply)

  lazy implicit val widgetKindFormat: Format[WidgetKind] = polymorphicTypeFormat(
    data => {
      case "DelayedTransformingWidget" => data.as[DelayedTransformingWidget]
      case "HealLocationWidget" => data.as[HealLocationWidget]
      case "SummonCreatureWidget" => data.as[SummonCreatureWidget]
      case "TimedExplosiveWidget" => data.as[TimedExplosiveWidget]
      case "TriggeredTransformingWidget" => data.as[TriggeredTransformingWidget]
      case "TriggeredTrapWidget" => data.as[TriggeredTrapWidget]
    }, {
      case widget: DelayedTransformingWidget => delayedTransformingFormat.writes(widget)
      case widget: HealLocationWidget => healLocationFormat.writes(widget)
      case widget: SummonCreatureWidget => summonCreatureFormat.writes(widget)
      case widget: TimedExplosiveWidget => timedExplosiveWidgetFormat.writes(widget)
      case widget: TriggeredTransformingWidget => triggeredTransformingFormat.writes(widget)
      case widget: TriggeredTrapWidget => triggeredTrapFormat.writes(widget)
    }
  )

  lazy val widgetKindMapReads: Reads[Map[WidgetKindId, WidgetKind]] = mapReads

  lazy val widgetCategoryFormat: Format[Category] = polymorphicObjectFormat({
    case "WidgetKind.HealCategory" => WidgetKind.HealCategory
    case "WidgetKind.PortalCategory" => WidgetKind.PortalCategory
    case "WidgetKind.TrapCategory" => WidgetKind.TrapCategory
  })
}
