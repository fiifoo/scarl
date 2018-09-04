package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureBehaviorMutation(creature: CreatureId, behavior: Behavior) extends Mutation {

  def apply(s: State): State = {
    val mutated = creature(s).copy(behavior = behavior)

    s.copy(entities = s.entities + (mutated.id -> mutated))
  }
}
