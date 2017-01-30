package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.entity._

object State {

  case class Index(containerItems: Map[EntityId, List[ItemId]] = Map(),
                   locationEntities: Map[Location, List[LocatableId]] = Map(),
                   targetStatuses: Map[EntityId, List[StatusId]] = Map()
                  )

  case class Temporary(addedActors: List[ActorId] = List(),
                       removableEntities: List[EntityId] = List()
                      )

}

case class State(entities: Map[EntityId, Entity] = Map(),
                 index: State.Index = State.Index(),
                 nextEntityId: Int = 1,
                 seed: Int = 1,
                 tactics: Map[CreatureId, Tactic] = Map(),
                 tick: Int = 1,
                 tmp: State.Temporary = State.Temporary()
                )
