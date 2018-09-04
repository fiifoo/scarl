package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}

case class CreatureOwnerMutation(creature: CreatureId, owner: Option[SafeCreatureId]) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(owner = owner)

    s.copy(
      entities = s.entities + (mutated.id -> mutated)
    )
  }
}
