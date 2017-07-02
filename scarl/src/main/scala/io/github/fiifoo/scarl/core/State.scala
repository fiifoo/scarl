package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.power.Powers
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}

object State {

  case class Cache(equipmentStats: Map[CreatureId, Stats] = Map())

  case class Communications(data: Map[CommunicationId, Communication] = Map(),
                            received: Map[CreatureId, Set[CommunicationId]] = Map()
                           )

  case class Index(containerItems: Map[EntityId, Set[ItemId]] = Map(),
                   factionMembers: Map[FactionId, Set[CreatureId]] = Map(),
                   locationConduit: Map[Location, ConduitId] = Map(),
                   locationEntities: Map[Location, Set[LocatableId]] = Map(),
                   locationTriggers: Map[Location, Set[TriggerStatusId]] = Map(),
                   targetStatuses: Map[EntityId, Set[StatusId]] = Map()
                  )

  case class Stored(actors: List[ActorId] = List())

  case class Temporary(addedActors: List[ActorId] = List(),
                       conduitEntry: Option[(ConduitId, Traveler)] = None,
                       removableEntities: Set[EntityId] = Set()
                      )

}

case class State(cache: State.Cache = State.Cache(),
                 communications: State.Communications = State.Communications(),
                 conduits: Map[ConduitId, Location] = Map(),
                 entities: Map[EntityId, Entity] = Map(),
                 equipments: Map[CreatureId, Map[Slot, ItemId]] = Map(),
                 factions: Map[FactionId, Faction] = Map(),
                 gateways: Set[Location] = Set(),
                 index: State.Index = State.Index(),
                 kinds: Kinds = Kinds(),
                 nextEntityId: Int = 1,
                 powers: Powers = Powers(),
                 progressions: Map[ProgressionId, Progression] = Map(),
                 rng: Rng = Rng(1),
                 stored: State.Stored = State.Stored(),
                 tactics: Map[CreatureId, Tactic] = Map(),
                 tick: Int = 1,
                 tmp: State.Temporary = State.Temporary()
                )
