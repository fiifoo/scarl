package models.json

import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.kind.ItemKindId
import play.api.libs.json._

object JsonArea {

  import JsonBase.{mapReads, stringIdFormat, tuple3Format}

  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat

  lazy implicit val areaIdFormat: Format[AreaId] = stringIdFormat(_.value, AreaId.apply)

  implicitly(tuple3Format[AreaId, ItemKindId, ItemKindId])

  lazy implicit val areaReads: Reads[Area] = Json.reads

  lazy val areaMapReads: Reads[Map[AreaId, Area]] = mapReads
}
