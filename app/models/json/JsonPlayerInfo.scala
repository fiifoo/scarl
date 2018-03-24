package models.json

import io.github.fiifoo.scarl.game.player.PlayerInfo
import play.api.libs.json._

object JsonPlayerInfo {
  lazy private implicit val creatureFormat = JsonCreature.creatureFormat
  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat
  lazy private implicit val keyFormat = JsonItemKey.keyFormat

  lazy val playerInfoWrites: Writes[PlayerInfo] = Json.writes[PlayerInfo]
}
