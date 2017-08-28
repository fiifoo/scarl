package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.index._

case class NewEntityMutation(entity: Entity, existing: Boolean = false) extends Mutation {

  def apply(s: State): State = {
    if (s.entities.isDefinedAt(entity.id)) {
      throw new Exception(s"EntityId ${entity.id} is already used.")
    }

    val actorQueue = entity match {
      case actor: Actor => s.cache.actorQueue.enqueue(actor)
      case _ => s.cache.actorQueue
    }

    val simulation = if (s.simulation.running) {
      s.simulation.copy(entities = s.simulation.entities + entity.id)
    } else {
      s.simulation
    }

    s.copy(
      cache = s.cache.copy(actorQueue = actorQueue),
      entities = s.entities + (entity.id -> entity),
      index = NewEntityIndexMutation(entity)(s, s.index),
      simulation = simulation,
    )
  }
}
