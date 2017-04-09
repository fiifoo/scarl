package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId

abstract class ActionValidator[A <: Action] {
  def apply(s: State, actor: CreatureId, action: A): Boolean
}
