package models.json

import io.github.fiifoo.scarl.core.kind._
import play.api.libs.json._

object JsonKind {

  import JsonBase.polymorphicIdFormat

  lazy private implicit val creatureKindMapReads = JsonCreatureKind.creatureKindMapReads
  lazy private implicit val itemKindMapReads = JsonItemKind.itemKindMapReads
  lazy private implicit val terrainKindMapReads = JsonTerrainKind.terrainKindMapReads
  lazy private implicit val wallKindMapReads = JsonWallKind.wallKindMapReads
  lazy private implicit val widgetKindMapReads = JsonWidgetKind.widgetKindMapReads

  lazy val kindIdFormat: Format[KindId] = polymorphicIdFormat[KindId, String](
    value => name => {
      (name match {
        case "CreatureKind" => CreatureKindId(value)
        case "ItemKind" => ItemKindId(value)
        case "TerrainKind" => TerrainKindId(value)
        case "WallKind" => WallKindId(value)
        case "WidgetKind" => WidgetKindId(value)
        case "CreatureKindId" => CreatureKindId(value)
        case "ItemKindId" => ItemKindId(value)
        case "TerrainKindId" => TerrainKindId(value)
        case "WallKindId" => WallKindId(value)
        case "WidgetKindId" => WidgetKindId(value)
      }).asInstanceOf[KindId]
    }, {
      id => JsString(id.value)
    }
  )

  lazy val kindsReads: Reads[Kinds] = Json.reads
}
