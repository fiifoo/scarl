package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureEnergyMutation(creature: CreatureId, energy: Double) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(energy = energy)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
