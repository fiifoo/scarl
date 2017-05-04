package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId

class NullValidator[A <: Action] extends ActionValidator[A] {
  def apply(s: State, actor: CreatureId, action: A): Boolean = true
}
