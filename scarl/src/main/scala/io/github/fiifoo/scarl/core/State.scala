package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.{ActorId, Entity, EntityId}

case class State(entities: Map[EntityId, Entity] = Map(),
                 index: StateIndex = StateIndex(),
                 nextEntityId: Int = 1,
                 seed: Int = 1,
                 tick: Int = 1,
                 tmp: TmpState = TmpState()
                )

case class TmpState(addedActors: List[ActorId] = List(), removableEntities: List[EntityId] = List())
