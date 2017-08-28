package game.json

import game.json.FormatBase._
import game.json.FormatEntities._
import game.json.FormatId._
import game.json.FormatItem.formatSlot
import game.json.FormatTactic._
import game.json.FormatUtils._
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.{Assets, IdSeq, Rng, State}
import play.api.libs.json._

object FormatState {
  // recalculated
  implicit val formatStateCache = formatEmpty(State.Cache())
  implicit val formatStateIndex = formatEmpty(State.Index())
  // reset from game data
  implicit val formatStateAssets = formatEmpty(Assets())
  // should be empty
  implicit val formatStateSimulation = formatEmpty(State.Simulation())
  implicit val formatStateTemporary = formatEmpty(State.Temporary())

  implicit val formatStateArea = Json.format[State.Area]
  implicit val formatStateConduits = formatMap(formatConduitId, formatLocation)
  implicit val formatStateEquipments = formatMap(formatCreatureId, formatMap(formatSlot, formatItemId))
  implicit val formatStateIdSeq = Json.format[IdSeq]
  implicit val formatStateReceivedCommunications = formatMap(formatCreatureId, implicitly[Format[Set[CommunicationId]]])
  implicit val formatStateRng = Json.format[Rng]
  implicit val formatStateTactics = formatMap(formatCreatureId, formatTactic)

  implicit val formatState = Json.format[State]
}
