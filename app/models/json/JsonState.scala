package models.json

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.ai.{Brain, Tactic}
import io.github.fiifoo.scarl.core.assets.Assets
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.{CreatureId, IdSeq, ItemId, UsableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.ConduitId
import play.api.libs.json._

object JsonState {

  import JsonBase.{emptyFormat, mapFormat, tuple2Format}

  // reset from game data
  lazy private implicit val assetsFormat = emptyFormat(Assets())
  // recalculated
  lazy private implicit val cacheFormat = emptyFormat(State.Cache())
  lazy private implicit val indexFormat = emptyFormat(State.Index())
  // should be empty
  lazy private implicit val simulationFormat = emptyFormat(State.Simulation())
  lazy private implicit val temporaryFormat = emptyFormat(State.Temporary())

  lazy private implicit val brainFormat = JsonBrain.brainFormat
  lazy private implicit val communicationIdFormat = JsonCommunication.communicationIdFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val goalIdFormat = JsonGoal.goalIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemIdFormat = JsonItem.itemIdFormat
  lazy private implicit val keyFormat = JsonItemKey.keyFormat
  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val machineryFormat = JsonMachinery.machineryFormat
  lazy private implicit val machineryIdFormat = JsonMachinery.machineryIdFormat
  lazy private implicit val recipeIdFormat = JsonRecipe.recipeIdFormat
  lazy private implicit val signalFormat = JsonSignal.signalFormat
  lazy private implicit val slotFormat = JsonItemEquipment.slotFormat
  lazy private implicit val tacticFormat = JsonTactic.tacticFormat

  lazy private implicit val areaFormat = Json.format[State.Area]
  lazy private implicit val conduitsFormat = Json.format[State.Conduits]

  implicitly(mapFormat[FactionId, Set[CommunicationId]])
  implicitly(mapFormat[CreatureId, List[ItemKindId]])
  implicitly(mapFormat[CreatureId, Map[CreatureId, List[ItemKindId]]])
  implicitly(mapFormat[CreatureId, List[Location]])
  lazy private implicit val conversationTarget = {
    implicit val usableId = JsonEntity.usableIdFormat

    implicitly(tuple2Format[UsableId, CommunicationId])
  }
  implicitly(mapFormat[CreatureId, (UsableId, CommunicationId)])
  lazy private implicit val stateCreatureFormat = Json.format[State.Creature]

  implicitly(mapFormat[FactionId, Brain])
  implicitly(mapFormat[ConduitId, Location])
  lazy private implicit val entityMapFormat = JsonEntity.entityMapFormat
  implicitly(mapFormat[Slot, ItemId])
  implicitly(mapFormat[CreatureId, Map[Slot, ItemId]])
  lazy private implicit val idSeqFormat = Json.format[IdSeq]
  implicitly(mapFormat[CreatureId, Set[Key]])
  implicitly(mapFormat[CreatureId, Map[CreatureId, Set[CommunicationId]]])
  implicitly(mapFormat[CreatureId, Set[RecipeId]])
  lazy private implicit val rngFormat = Json.format[Rng]
  implicitly(mapFormat[CreatureId, Tactic])

  lazy val stateFormat = Json.format[State]
}
