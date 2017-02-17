package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.Kinds

object State {

  case class Index(containerItems: Map[EntityId, List[ItemId]] = Map(),
                   factionMembers: Map[FactionId, List[CreatureId]] = Map(),
                   locationEntities: Map[Location, List[LocatableId]] = Map(),
                   locationTriggers: Map[Location, List[TriggerStatusId]] = Map(),
                   targetStatuses: Map[EntityId, List[StatusId]] = Map()
                  )

  case class Temporary(addedActors: List[ActorId] = List(),
                       removableEntities: List[EntityId] = List()
                      )

}

case class State(entities: Map[EntityId, Entity] = Map(),
                 factions: Map[FactionId, Faction] = Map(),
                 index: State.Index = State.Index(),
                 kinds: Kinds = Kinds(),
                 nextEntityId: Int = 1,
                 rng: Rng = Rng(1),
                 tactics: Map[CreatureId, Tactic] = Map(),
                 tick: Int = 1,
                 tmp: State.Temporary = State.Temporary()
                )
