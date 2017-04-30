package models.json

import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.world.{Conduit, WorldState}
import models.json.FormatId._
import models.json.FormatState._
import models.json.FormatUtils._
import play.api.libs.json._

object FormatWorldState {
  implicit val formatConduit = Json.format[Conduit]

  // reset from game data
  implicit val formatWorldAreas = formatEmpty[Map[AreaId, Area]](Map())

  implicit val formatWorldConduits = formatMap(formatConduitId, formatConduit)
  implicit val formatWorldStates = formatMap(formatAreaId, formatState)
  implicit val formatWorldState = Json.format[WorldState]
}
