package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain
import io.github.fiifoo.scarl.core.creature.FactionId

case class BrainsMutation(brains: Map[FactionId, Brain]) extends Mutation {

  def apply(s: State): State = {
    s.copy(factions = s.factions.copy(
      brains = brains
    ))
  }
}
