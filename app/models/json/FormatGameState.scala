package models.json

import io.github.fiifoo.scarl.game.GameState
import io.github.fiifoo.scarl.game.map.MapLocation
import models.json.FormatBase._
import models.json.FormatId._
import models.json.FormatUtils._
import models.json.FormatWorldState._
import play.api.libs.json._

object FormatGameState {
  implicit val formatMapLocation = Json.format[MapLocation]
  implicit val formatAreaMap = formatMap(formatLocation, formatMapLocation)
  implicit val formatAreaMaps = formatMap(formatAreaId, formatAreaMap)

  implicit val formatGameState = Json.format[GameState]
}
