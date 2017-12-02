package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureMaterialsMutation(creature: CreatureId, materials: Double) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(materials = materials)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
