package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId

trait CreaturePower {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId] = None): List[Effect]
}
