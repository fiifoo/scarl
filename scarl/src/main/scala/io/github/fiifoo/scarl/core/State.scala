package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.ai.{Brain, Strategy, Tactic}
import io.github.fiifoo.scarl.core.assets.Assets
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, Sector, WaypointNetwork}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.{ConduitId, GoalId}

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
                      exits: Map[ConduitId, Location] = Map(),
                     )

  case class Creature(conversations: Map[CreatureId, (UsableId, CommunicationId)] = Map(),
                      equipments: Map[CreatureId, Map[Slot, ItemId]] = Map(),
                      foundItems: Map[CreatureId, Set[ItemId]] = Map(),
                      goals: Set[GoalId] = Set(),
                      keys: Map[CreatureId, Set[Key]] = Map(),
                      receivedCommunications: Map[FactionId, Set[CommunicationId]] = Map(),
                      recipes: Map[CreatureId, Set[RecipeId]] = Map(),
                      recycledItems: Map[CreatureId, List[ItemKindId]] = Map(),
                      tactics: Map[CreatureId, Tactic] = Map(),
                      trails: Map[CreatureId, List[Location]] = Map(),
                     )

  case class Factions(brains: Map[FactionId, Brain] = Map(),
                      dispositions: Map[FactionId, Map[FactionId, Disposition]] = Map(),
                      strategies: Map[FactionId, Strategy] = Map(),
                     )

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

  case class Temporary(conduitEntry: Option[List[(CreatureId, ConduitId)]] = None,
                       removableEntities: Set[EntityId] = Set(),
                       waypointNetworkChanged: Set[Location] = Set(),
                      )

}

case class State(area: State.Area = State.Area(),
                 assets: Assets = Assets(),
                 cache: State.Cache = State.Cache(),
                 conduits: State.Conduits = State.Conduits(),
                 creature: State.Creature = State.Creature(),
                 factions: State.Factions = State.Factions(),
                 entities: Map[EntityId, Entity] = Map(),
                 gateways: Set[Location] = Set(),
                 idSeq: IdSeq = IdSeq(1),
                 index: State.Index = State.Index(),
                 rng: Rng = Rng(1),
                 signals: List[Signal] = List(),
                 simulation: State.Simulation = State.Simulation(),
                 tick: Tick = 1,
                 tmp: State.Temporary = State.Temporary(),
                )
