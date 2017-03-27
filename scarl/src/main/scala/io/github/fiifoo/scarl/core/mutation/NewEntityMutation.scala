package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.index._

case class NewEntityMutation(entity: Entity) extends Mutation {

  def apply(s: State): State = {
    if (entity.id.value != s.nextEntityId) {
      throw new Exception(s"${entity.id} does not match state next entity id")
    }

    val addedActors = entity match {
      case actor: Actor => actor.id :: s.tmp.addedActors
      case _ => s.tmp.addedActors
    }

    s.copy(
      entities = s.entities + (entity.id -> entity),
      index = NewEntityIndexMutation(entity)(s, s.index),
      nextEntityId = entity.id.value + 1,
      tmp = s.tmp.copy(addedActors = addedActors)
    )
  }
}
