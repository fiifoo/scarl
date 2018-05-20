package models.json

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, WidgetKindId}
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice
import play.api.libs.json._

object JsonTheme {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  lazy private implicit val creatureChoiceReads = Json.reads[WeightedChoice[CreatureKindId]]
  lazy private implicit val itemChoiceReads = Json.reads[WeightedChoice[ItemKindId]]
  lazy private implicit val templateChoiceReads = Json.reads[WeightedChoice[TemplateId]]
  lazy private implicit val widgetChoiceReads = Json.reads[WeightedChoice[WidgetKindId]]

  lazy implicit val themeIdFormat: Format[ThemeId] = stringIdFormat(_.value, ThemeId.apply)

  lazy implicit val themeReads: Reads[Theme] = Json.reads

  lazy val themeMapReads: Reads[Map[ThemeId, Theme]] = mapReads
}
