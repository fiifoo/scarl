package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureDeadMutation(creature: CreatureId, dead: Boolean = true) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(dead = dead)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
