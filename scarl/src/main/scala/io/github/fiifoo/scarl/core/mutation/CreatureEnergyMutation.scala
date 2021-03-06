package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureEnergyMutation(creature: CreatureId, energy: Double) extends Mutation {

  def apply(s: State): State = {
    val current = creature(s)
    val mutated = current.copy(resources = current.resources.copy(
      energy = energy
    ))

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
