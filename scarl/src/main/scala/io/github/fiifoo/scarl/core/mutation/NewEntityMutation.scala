package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.index._

case class NewEntityMutation(entity: Entity, existing: Boolean = false) extends Mutation {

  def apply(s: State): State = {
    if (!existing && entity.id.value != s.nextEntityId) {
      throw new Exception(s"Entity id ${entity.id} does not match state next entity id")
    }
    if (s.entities.isDefinedAt(entity.id)) {
      throw new Exception(s"EntityId ${entity.id} is already used.")
    }

    val addedActors = entity match {
      case actor: Actor => actor.id :: s.tmp.addedActors
      case _ => s.tmp.addedActors
    }

    s.copy(
      entities = s.entities + (entity.id -> entity),
      index = NewEntityIndexMutation(entity)(s, s.index),
      nextEntityId = if (existing) s.nextEntityId else entity.id.value + 1,
      tmp = s.tmp.copy(addedActors = addedActors)
    )
  }
}
