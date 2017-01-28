package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}

case class TestMoveTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    val from = actor(s).location
    val to = Location(from.x + 1, from.y)

    (this, TestMoveAction(to))
  }
}
