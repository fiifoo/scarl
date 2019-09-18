package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.item.Key

case class CreatureKeysMutation(creature: CreatureId, keys: Set[Key]) extends Mutation {

  def apply(s: State): State = {
    s.copy(creature = s.creature.copy(
      keys = s.creature.keys + (creature -> keys)
    ))
  }
}
