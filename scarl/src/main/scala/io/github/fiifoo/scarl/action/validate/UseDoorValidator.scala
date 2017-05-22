package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.UseDoorAction
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}

object UseDoorValidator extends ActionValidator[UseDoorAction] {
  def apply(s: State, actor: CreatureId, action: UseDoorAction): Boolean = {
    if (!entityExists(s, action.target) || action.target(s).door.isEmpty) {
      return false
    }

    getLocation(s, action.target).exists(adjacentLocation(s, actor, _))
  }

  private def getLocation(s: State, door: ItemId): Option[Location] = {
    door(s).container match {
      case container: ContainerId => Some(container(s).location)
      case _ => None
    }
  }
}
