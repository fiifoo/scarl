package models.json

import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.GameState
import io.github.fiifoo.scarl.game.area.MapLocation
import io.github.fiifoo.scarl.world.SiteId
import play.api.libs.json._

object JsonGameState {

  import JsonBase.mapFormat

  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val worldStateFormat = JsonWorldState.worldStateFormat
  lazy private implicit val mapLocationMapFormat = JsonMapLocation.mapLocationMapFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  implicitly(mapFormat[SiteId, Map[Location, MapLocation]])
  lazy private implicit val playerSettingsFormat = JsonPlayerSettings.playerSettingsFormat
  lazy private implicit val statisticsFormat = JsonStatistics.statisticsFormat

  lazy val gameStateFormat = Json.format[GameState]
}
