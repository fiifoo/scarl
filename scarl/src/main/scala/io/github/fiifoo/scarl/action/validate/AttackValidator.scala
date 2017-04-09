package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.AttackAction
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

object AttackValidator extends ActionValidator[AttackAction] {
  def apply(s: State, actor: CreatureId, action: AttackAction): Boolean = {
    entityExists(s, action.target) &&
      adjacentLocation(s, actor, action.target(s).location)
  }
}
