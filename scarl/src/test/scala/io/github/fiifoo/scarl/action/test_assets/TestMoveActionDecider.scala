package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.{Location, State}

object TestMoveActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val from = actor.location
    val to = Location(from.x + 1, from.y + 1)

    MoveAction(to)
  }
}
