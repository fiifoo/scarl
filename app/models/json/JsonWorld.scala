package models.json

import io.github.fiifoo.scarl.world.{World, WorldId}
import play.api.libs.json._

object JsonWorld {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat

  lazy implicit val worldIdFormat: Format[WorldId] = stringIdFormat(_.value, WorldId.apply)

  lazy private implicit val conduitSourceReads = Json.reads[World.ConduitSource]

  lazy implicit val worldReads: Reads[World] = Json.reads

  lazy val worldMapReads: Reads[Map[WorldId, World]] = mapReads
}