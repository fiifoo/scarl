package game.json

import game.json.FormatId._
import game.json.FormatState._
import game.json.FormatUtils._
import io.github.fiifoo.scarl.world.{Conduit, WorldAssets, WorldState}
import play.api.libs.json._

object FormatWorldState {
  implicit val formatConduit = Json.format[Conduit]

  // reset from game data
  implicit val formatWorldAssets = formatEmpty[WorldAssets](WorldAssets())

  implicit val formatWorldConduits = formatMap(formatConduitId, formatConduit)
  implicit val formatWorldStates = formatMap(formatAreaId, formatState)
  implicit val formatWorldState = Json.format[WorldState]
}
