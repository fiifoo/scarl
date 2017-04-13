package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureLevelMutation(creature: CreatureId, level: Int) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(level = level)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
