package models.json

import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.Template.{FixedContent, RandomizedContent}
import io.github.fiifoo.scarl.area.template.{FixedTemplate, RandomizedTemplate, Template, TemplateId}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind.ItemKindId
import play.api.libs.json._

object JsonTemplate {

  import JsonBase.{mapReads, optionReads, polymorphicTypeReads, stringIdFormat, tuple2Format, tuple3Format}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val locationReads = Json.reads[Location]
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  implicitly(optionReads[ItemKindId])
  implicitly(tuple2Format[Int, Int])
  implicitly(tuple3Format[TemplateId, Int, Int])

  lazy private implicit val rectangleReads = Json.reads[Rectangle]
  lazy private implicit val shapeReads: Reads[Shape] = polymorphicTypeReads(data => {
    case "Rectangle" => data.as[Rectangle]
  })

  lazy private implicit val fixedContentReads = Json.reads[FixedContent]
  lazy private implicit val randomizedContentReads = Json.reads[RandomizedContent]

  lazy private implicit val fixedTemplateReads = Json.reads[FixedTemplate]
  lazy private implicit val randomizedTemplateReads = Json.reads[RandomizedTemplate]

  lazy implicit val templateIdFormat: Format[TemplateId] = stringIdFormat(_.value, TemplateId.apply)

  lazy implicit val templateReads: Reads[Template] = polymorphicTypeReads(data => {
    case "FixedTemplate" => data.as[FixedTemplate]
    case "RandomizedTemplate" => data.as[RandomizedTemplate]
  })

  lazy val templateMapReads: Reads[Map[TemplateId, Template]] = mapReads
}
