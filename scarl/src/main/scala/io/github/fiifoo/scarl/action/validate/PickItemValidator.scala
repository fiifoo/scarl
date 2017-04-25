package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.PickItemAction
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}

object PickItemValidator extends ActionValidator[PickItemAction] {
  def apply(s: State, actor: CreatureId, action: PickItemAction): Boolean = {
    if (!entityExists(s, action.item)) {
      return false
    }

    val item = action.item(s)

    val validContainer = item.container match {
      case container: ContainerId => container(s).location == actor(s).location
      case _ => false
    }

    validContainer && item.pickable
  }
}
