package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.State.Stored
import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.character.{Progression, ProgressionId}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.Kinds

object State {

  case class Index(containerItems: Map[EntityId, Set[ItemId]] = Map(),
                   factionMembers: Map[FactionId, Set[CreatureId]] = Map(),
                   locationEntities: Map[Location, Set[LocatableId]] = Map(),
                   locationTriggers: Map[Location, Set[TriggerStatusId]] = Map(),
                   targetStatuses: Map[EntityId, Set[StatusId]] = Map()
                  )

  case class Stored(actors: List[ActorId] = List())

  case class Temporary(addedActors: List[ActorId] = List(),
                       conduitEntry: Option[(ConduitId, Creature)] = None,
                       removableEntities: List[EntityId] = List()
                      )

}

case class State(entities: Map[EntityId, Entity] = Map(),
                 conduits: Map[ConduitId, Location] = Map(),
                 factions: Map[FactionId, Faction] = Map(),
                 gateways: Set[Location] = Set(),
                 index: State.Index = State.Index(),
                 kinds: Kinds = Kinds(),
                 nextEntityId: Int = 1,
                 progressions: Map[ProgressionId, Progression] = Map(),
                 rng: Rng = Rng(1),
                 stored: Stored = Stored(),
                 tactics: Map[CreatureId, Tactic] = Map(),
                 tick: Int = 1,
                 tmp: State.Temporary = State.Temporary()
                )
