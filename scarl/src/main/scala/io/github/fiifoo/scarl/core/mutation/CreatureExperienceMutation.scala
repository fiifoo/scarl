package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureExperienceMutation(creature: CreatureId, experience: Int) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(experience = experience)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
