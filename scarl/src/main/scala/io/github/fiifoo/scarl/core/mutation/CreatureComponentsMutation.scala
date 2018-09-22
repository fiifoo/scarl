package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureComponentsMutation(creature: CreatureId, components: Int) extends Mutation {

  def apply(s: State): State = {
    val current = creature(s)
    val mutated = current.copy(resources = current.resources.copy(
      components = components
    ))


    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
