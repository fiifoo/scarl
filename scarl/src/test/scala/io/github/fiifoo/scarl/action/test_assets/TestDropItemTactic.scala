package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.DropItemAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item}
import io.github.fiifoo.scarl.core.{Rng, State}

case class TestDropItemTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    val inventory = s.entities.values collect {
      case i: Item if i.container == actor => i
    }

    (this, DropItemAction(inventory.head.id), rng)
  }
}
