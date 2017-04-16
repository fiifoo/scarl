package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.CommunicateAction
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

object CommunicateValidator extends ActionValidator[CommunicateAction] {
  def apply(s: State, actor: CreatureId, action: CommunicateAction): Boolean = {
    entityExists(s, action.target) &&
      adjacentLocation(s, actor, action.target(s).location)
  }
}
