package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.ai.{Brain, Tactic}
import io.github.fiifoo.scarl.core.assets.Assets
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, Sector, WaypointNetwork}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}

object State {

  case class Area(width: Int = 10,
                  height: Int = 10,
                  sectorSize: Int = 10,
                  owner: Option[FactionId] = None,
                 )

  case class Cache(actorQueue: ActorQueue = ActorQueue(),
                   equipmentStats: Map[CreatureId, Stats] = Map(),
                   waypointNetwork: WaypointNetwork = WaypointNetwork(),
                  )

  case class Conduits(entrances: Map[ConduitId, Location] = Map(),
                      exits: Map[ConduitId, Location] = Map()
                     )

  // todo: move foundItems, equipments, keys, receivedCommunications, recipes and tactics here
  case class Creature(recycledItems: Map[CreatureId, List[ItemKindId]] = Map())

  case class Index(containerItems: Map[EntityId, Set[ItemId]] = Map(),
                   factionMembers: Map[FactionId, Set[CreatureId]] = Map(),
                   itemFinders: Map[ItemId, Set[CreatureId]] = Map(),
                   locationConduit: Map[Location, ConduitId] = Map(),
                   locationEntities: Map[Location, Set[LocatableId]] = Map(),
                   locationMachinery: Map[Location, Set[MachineryId]] = Map(),
                   locationTriggers: Map[Location, Set[TriggerStatusId]] = Map(),
                   partyMembers: Map[Party, Set[CreatureId]] = Map(),
                   sectorCreatures: Map[Sector, Set[CreatureId]] = Map(),
                   sectorItems: Map[Sector, Set[ItemId]] = Map(),
                   targetStatuses: Map[EntityId, Set[StatusId]] = Map(),
                  )

  case class Simulation(running: Boolean = false,
                        entities: Set[EntityId] = Set()
                       )

  case class Temporary(conduitEntry: Option[(ConduitId, Traveler)] = None,
                       removableEntities: Set[EntityId] = Set(),
                       waypointNetworkChanged: Set[Location] = Set(),
                      )

}

case class State(area: State.Area = State.Area(),
                 assets: Assets = Assets(),
                 brains: Map[FactionId, Brain] = Map(),
                 cache: State.Cache = State.Cache(),
                 conduits: State.Conduits = State.Conduits(),
                 creature: State.Creature = State.Creature(),
                 foundItems: Map[CreatureId, Set[ItemId]] = Map(),
                 entities: Map[EntityId, Entity] = Map(),
                 equipments: Map[CreatureId, Map[Slot, ItemId]] = Map(),
                 gateways: Set[Location] = Set(),
                 idSeq: IdSeq = IdSeq(1),
                 index: State.Index = State.Index(),
                 keys: Map[CreatureId, Set[Key]] = Map(),
                 receivedCommunications: Map[CreatureId, Set[CommunicationId]] = Map(),
                 recipes: Map[CreatureId, Set[RecipeId]] = Map(),
                 rng: Rng = Rng(1),
                 signals: List[Signal] = List(),
                 simulation: State.Simulation = State.Simulation(),
                 tactics: Map[CreatureId, Tactic] = Map(),
                 tick: Tick = 1,
                 tmp: State.Temporary = State.Temporary(),
                )
