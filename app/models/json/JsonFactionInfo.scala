package models.json

import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.game.area.FactionInfo
import play.api.libs.json._

object JsonFactionInfo {

  import models.json.JsonBase.mapFormat

  lazy private implicit val dispositionFormat = JsonFaction.dispositionFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat

  implicitly(mapFormat[FactionId, Disposition])
  implicitly(mapFormat[FactionId, Map[FactionId, Disposition]])

  lazy val factionInfoWrites: Writes[FactionInfo] = Json.writes[FactionInfo]
}
