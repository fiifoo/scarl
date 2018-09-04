package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, CreaturePower}

case class CreatureUsableMutation(creature: CreatureId, usable: Option[CreaturePower]) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(usable = usable)

    s.copy(
      entities = s.entities + (mutated.id -> mutated)
    )
  }
}
