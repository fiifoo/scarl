package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.DropItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item}

import scala.util.Random

case object TestDropItemTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val inventory = s.entities.values collect {
      case i: Item if i.container == actor => i
    }

    (this, DropItemAction(inventory.head.id))
  }
}
