package io.github.fiifoo.scarl.core.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Creature

trait ActionDecider {
  def apply(s: State, actor: Creature): Action
}
