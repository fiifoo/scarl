package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

object PassValidator extends ActionValidator[PassAction] {
  def apply(s: State, actor: CreatureId, action: PassAction): Boolean = {
    true
  }
}
