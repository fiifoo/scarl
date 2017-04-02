package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ActorId

case class StoredActorsMutation(actors: List[ActorId]) extends Mutation {

  def apply(s: State): State = {
    if (s.stored.actors.nonEmpty) {
      throw new Exception("Stored actors already set. Cannot override.")
    }

    s.copy(stored = s.stored.copy(
      actors = actors
    ))
  }
}
