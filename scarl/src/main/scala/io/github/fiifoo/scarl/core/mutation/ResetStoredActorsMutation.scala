package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State

case class ResetStoredActorsMutation() extends Mutation {

  def apply(s: State): State = {
    s.copy(stored = s.stored.copy(
      actors = List()
    ))
  }
}
