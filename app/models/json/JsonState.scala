package models.json

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.ai.Tactic
import io.github.fiifoo.scarl.core.assets.Assets
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity.{CreatureId, IdSeq, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.ConduitId
import play.api.libs.json._

object JsonState {

  import JsonBase.{emptyFormat, mapFormat}

  // reset from game data
  lazy private implicit val assetsFormat = emptyFormat(Assets())
  // recalculated
  lazy private implicit val cacheFormat = emptyFormat(State.Cache())
  lazy private implicit val indexFormat = emptyFormat(State.Index())
  // should be empty
  lazy private implicit val simulationFormat = emptyFormat(State.Simulation())
  lazy private implicit val temporaryFormat = emptyFormat(State.Temporary())

  lazy private implicit val communicationIdFormat = JsonCommunication.communicationIdFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val itemIdFormat = JsonItem.itemIdFormat
  lazy private implicit val keyFormat = JsonKey.keyFormat
  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val machineryFormat = JsonMachinery.machineryFormat
  lazy private implicit val machineryIdFormat = JsonMachinery.machineryIdFormat
  lazy private implicit val slotFormat = JsonItemEquipment.slotFormat
  lazy private implicit val tacticFormat = JsonTactic.tacticFormat

  lazy private implicit val areaFormat = Json.format[State.Area]
  implicitly(mapFormat[ConduitId, Location])
  lazy private implicit val entityMapFormat = JsonEntity.entityMapFormat
  implicitly(mapFormat[Slot, ItemId])
  implicitly(mapFormat[CreatureId, Map[Slot, ItemId]])
  lazy private implicit val idSeqFormat = Json.format[IdSeq]
  implicitly(mapFormat[CreatureId, Set[Key]])
  implicitly(mapFormat[CreatureId, Map[CreatureId, Set[CommunicationId]]])
  lazy private implicit val rngFormat = Json.format[Rng]
  implicitly(mapFormat[CreatureId, Tactic])

  lazy val stateFormat = Json.format[State]
}
