package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.EnterConduitAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

object EnterConduitValidator extends ActionValidator[EnterConduitAction] {
  def apply(s: State, actor: CreatureId, action: EnterConduitAction): Boolean = {
    actor(s).location == s.conduits(action.conduit)
  }
}
