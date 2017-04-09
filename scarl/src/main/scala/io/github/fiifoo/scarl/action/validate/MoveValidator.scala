package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

object MoveValidator extends ActionValidator[MoveAction] {
  def apply(s: State, actor: CreatureId, action: MoveAction): Boolean = {
    adjacentLocation(s, actor, action.location)
  }
}
