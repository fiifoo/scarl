package game.json

import game.json.FormatBase._
import game.json.FormatId._
import game.json.FormatUtils._
import game.json.FormatWorldState._
import io.github.fiifoo.scarl.game.map.MapLocation
import io.github.fiifoo.scarl.game.{GameState, Statistics}
import play.api.libs.json._

object FormatGameState {
  implicit val formatMapLocation = Json.format[MapLocation]
  implicit val formatAreaMap = formatMap(formatLocation, formatMapLocation)
  implicit val formatAreaMaps = formatMap(formatAreaId, formatAreaMap)


  implicit val formatCreatureCounter = formatMap(formatCreatureKindId, implicitly[Format[Int]])
  implicit val formatStatistics = Json.format[Statistics]

  implicit val formatGameState = Json.format[GameState]
}
