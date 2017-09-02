package models.json

import io.github.fiifoo.scarl.core.entity.Locatable
import io.github.fiifoo.scarl.core.kind._
import play.api.libs.json._

object JsonKind {

  import JsonBase.polymorphicIdReads

  lazy private implicit val creatureKindMapReads = JsonCreatureKind.creatureKindMapReads
  lazy private implicit val itemKindMapReads = JsonItemKind.itemKindMapReads
  lazy private implicit val terrainKindMapReads = JsonTerrainKind.terrainKindMapReads
  lazy private implicit val wallKindMapReads = JsonWallKind.wallKindMapReads
  lazy private implicit val widgetKindMapReads = JsonWidgetKind.widgetKindMapReads

  lazy val kindIdReads: Reads[KindId[Locatable]] = polymorphicIdReads[KindId[Locatable], String](
    value => name => {
      (name match {
        case "CreatureKind" => CreatureKindId(value)
        case "ItemKind" => CreatureKindId(value)
        case "TerrainKind" => CreatureKindId(value)
        case "WallKind" => CreatureKindId(value)
        case "WidgetKind" => CreatureKindId(value)
      }).asInstanceOf[KindId[Locatable]]
    }
  )

  lazy val kindsReads: Reads[Kinds] = Json.reads
}
