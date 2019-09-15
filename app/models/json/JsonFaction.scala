package models.json

import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import models.json.JsonBase.polymorphicObjectFormat
import play.api.libs.json._

object JsonFaction {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val strategyFormat = JsonStrategy.strategyFormat

  lazy implicit val dispositionFormat: Format[Disposition] = polymorphicObjectFormat({
    case "Faction.Friendly" => Faction.Friendly
    case "Faction.Neutral" => Faction.Neutral
    case "Faction.Hostile" => Faction.Hostile
  })

  lazy implicit val factionIdFormat: Format[FactionId] = stringIdFormat(_.value, FactionId.apply)

  lazy implicit val factionFormat: Format[Faction] = Json.format

  lazy val factionMapReads: Reads[Map[FactionId, Faction]] = mapReads
}
