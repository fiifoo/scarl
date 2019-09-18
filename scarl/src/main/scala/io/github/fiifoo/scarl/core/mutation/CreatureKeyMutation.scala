package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.item.Key

case class CreatureKeyMutation(creature: CreatureId, key: Key) extends Mutation {

  def apply(s: State): State = {
    val previous = s.creature.keys.getOrElse(creature, Set())
    val next = previous + key

    s.copy(creature = s.creature.copy(
      keys = s.creature.keys + (creature -> next)
    ))
  }
}
