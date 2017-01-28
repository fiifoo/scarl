package io.github.fiifoo.scarl.core.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

trait Tactic {
  val actor: CreatureId

  def apply(s: State): (Tactic, Action)
}
