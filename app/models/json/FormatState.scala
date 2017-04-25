package models.json

import io.github.fiifoo.scarl.core.State.Communications
import io.github.fiifoo.scarl.core.character.{Progression, ProgressionId}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.{Rng, State}
import models.json.FormatBase._
import models.json.FormatEntities._
import models.json.FormatEquipment.formatSlot
import models.json.FormatId._
import models.json.FormatTactic._
import models.json.FormatUtils._
import play.api.libs.json._

object FormatState {
  // recalculated
  implicit val formatStateIndex = formatEmpty(State.Index())
  // reset from game data
  implicit val formatStateCommunicationsData = formatEmpty[Map[CommunicationId, Communication]](Map())
  implicit val formatStateKinds = formatEmpty(Kinds())
  implicit val formatStateProgressions = formatEmpty[Map[ProgressionId, Progression]](Map())
  // should be empty
  implicit val formatStateTemporary = formatEmpty(State.Temporary())

  implicit val formatActorIdList = formatList(formatActorId)
  implicit val formatCreatureCommunications = formatMap(formatCreatureId, implicitly[Format[Set[CommunicationId]]])
  implicit val formatFaction = Json.format[Faction]

  implicit val formatStateCommunications = Json.format[Communications]
  implicit val formatStateConduits = formatMap(formatConduitId, formatLocation)
  implicit val formatEquipments = formatMap(formatCreatureId, formatMap(formatSlot, formatItemId))
  implicit val formatStateFactions = formatMap(formatFactionId, formatFaction)
  implicit val formatStateRng = Json.format[Rng]
  implicit val formatStateTactics = formatMap(formatCreatureId, formatTactic)
  implicit val formatStateStored = Json.format[State.Stored]

  implicit val formatState = Json.format[State]
}
