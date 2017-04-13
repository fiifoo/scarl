package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Creature.Stats
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureStatsMutation(creature: CreatureId, stats: Stats) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(stats = stats)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
