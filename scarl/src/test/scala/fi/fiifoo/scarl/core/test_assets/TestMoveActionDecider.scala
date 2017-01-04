package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.action.{Action, ActionDecider}
import fi.fiifoo.scarl.core.entity.Creature
import fi.fiifoo.scarl.core.{Location, State}

object TestMoveActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val from = actor.location
    val to = Location(from.x + 1, from.y)

    TestMoveAction(to)
  }
}
