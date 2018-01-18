package models.json

import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import play.api.libs.json._

object JsonFaction {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val strategyFormat = JsonStrategy.strategyFormat

  lazy implicit val factionIdFormat: Format[FactionId] = stringIdFormat(_.value, FactionId.apply)

  lazy implicit val factionFormat: Format[Faction] = Json.format

  lazy val factionMapReads: Reads[Map[FactionId, Faction]] = mapReads
}
