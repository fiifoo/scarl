package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.DropItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item}

case class TestDropItemTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    val items = s.entities filter (_._2.isInstanceOf[Item]) map (_._2.asInstanceOf[Item])
    val inventory = items filter (_.container == actor)
    val item = inventory.head

    (this, DropItemAction(item))
  }
}
