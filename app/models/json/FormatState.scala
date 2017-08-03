package models.json

import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature.{Faction, Progression, ProgressionId}
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.power.Powers
import io.github.fiifoo.scarl.core.{Rng, State}
import models.json.FormatBase._
import models.json.FormatEntities._
import models.json.FormatId._
import models.json.FormatItem.formatSlot
import models.json.FormatTactic._
import models.json.FormatUtils._
import play.api.libs.json._

object FormatState {
  // recalculated
  implicit val formatStateCache = formatEmpty(State.Cache())
  implicit val formatStateIndex = formatEmpty(State.Index())
  // reset from game data
  implicit val formatStateAssets = formatEmpty(State.Assets())
  implicit val formatStateCommunicationsData = formatEmpty[Map[CommunicationId, Communication]](Map())
  implicit val formatStateKinds = formatEmpty(Kinds())
  implicit val formatStatePowers = formatEmpty(Powers())
  implicit val formatStateProgressions = formatEmpty[Map[ProgressionId, Progression]](Map())
  // should be empty
  implicit val formatStateSimulation = formatEmpty(State.Simulation())
  implicit val formatStateTemporary = formatEmpty(State.Temporary())

  implicit val formatActorIdList = formatList(formatActorId)
  implicit val formatCreatureCommunications = formatMap(formatCreatureId, implicitly[Format[Set[CommunicationId]]])
  implicit val formatFaction = Json.format[Faction]

  implicit val formatStateArea = Json.format[State.Area]
  implicit val formatStateCommunications = Json.format[State.Communications]
  implicit val formatStateConduits = formatMap(formatConduitId, formatLocation)
  implicit val formatEquipments = formatMap(formatCreatureId, formatMap(formatSlot, formatItemId))
  implicit val formatStateFactions = formatMap(formatFactionId, formatFaction)
  implicit val formatStateRng = Json.format[Rng]
  implicit val formatStateTactics = formatMap(formatCreatureId, formatTactic)

  implicit val formatState = Json.format[State]
}
