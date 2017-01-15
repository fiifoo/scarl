package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.DropItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.{Creature, Item}

object TestDropItemActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val items = s.entities filter (_._2.isInstanceOf[Item]) map (_._2.asInstanceOf[Item])
    val inventory = items filter (_.container == actor.id)
    val item = inventory.head

    DropItemAction(item)
  }
}
