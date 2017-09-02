package models.json

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.game.GameState
import io.github.fiifoo.scarl.game.map.MapLocation
import play.api.libs.json._

object JsonGameState {

  import JsonBase.mapFormat

  lazy private implicit val areaIdFormat = JsonArea.areaIdFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val worldStateFormat = JsonWorldState.worldStateFormat
  lazy private implicit val mapLocationMapFormat = JsonMapLocation.mapLocationMapFormat
  implicitly(mapFormat[AreaId, Map[Location, MapLocation]])
  lazy implicit val statisticsFormat = JsonStatistics.statisticsFormat

  lazy val gameStateFormat = Json.format[GameState]
}
