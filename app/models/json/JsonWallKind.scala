package models.json

import io.github.fiifoo.scarl.core.kind.WallKind.Category
import io.github.fiifoo.scarl.core.kind.{WallKind, WallKindId}
import models.json.JsonBase.polymorphicObjectFormat
import play.api.libs.json._

object JsonWallKind {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val charFormat = JsonBase.charFormat

  lazy implicit val wallKindIdFormat: Format[WallKindId] = stringIdFormat(_.value, WallKindId.apply)

  lazy implicit val wallKindFormat: Format[WallKind] = Json.format

  lazy val wallKindMapReads: Reads[Map[WallKindId, WallKind]] = mapReads

  lazy val wallCategoryFormat: Format[Category] = polymorphicObjectFormat({
    case "WallKind.DefaultCategory" => WallKind.DefaultCategory
    case "WallKind.AreaBorderCategory" => WallKind.AreaBorderCategory
    case "WallKind.ConstructedCategory" => WallKind.ConstructedCategory
    case "WallKind.NaturalCategory" => WallKind.NaturalCategory
    case "WallKind.SecureCategory" => WallKind.SecureCategory
  })
}
