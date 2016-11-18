package fi.fiifoo.scarl.action.test_assets

import fi.fiifoo.scarl.action.DropItemAction
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.{Action, ActionDecider}
import fi.fiifoo.scarl.core.entity.{Creature, Item}

object TestDropItemActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val items = s.entities filter (_._2.isInstanceOf[Item]) map (_._2.asInstanceOf[Item])
    val inventory = items filter (_.container == actor.id)
    val item = inventory.head

    DropItemAction(item)
  }
}
