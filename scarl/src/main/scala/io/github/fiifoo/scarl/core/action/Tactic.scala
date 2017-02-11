package io.github.fiifoo.scarl.core.action

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Rng, State}

trait Tactic {
  val actor: CreatureId

  def apply(s: State, rng: Rng): (Tactic, Action, Rng)
}
