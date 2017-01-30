package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.DropItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item}

case class TestDropItemTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    val inventory = s.entities.values collect {
      case i: Item if i.container == actor => i
    }

    (this, DropItemAction(inventory.head.id))
  }
}
