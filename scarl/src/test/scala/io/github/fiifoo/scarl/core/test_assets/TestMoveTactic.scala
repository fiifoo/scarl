package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

case object TestMoveTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val from = actor(s).location
    val to = Location(from.x + 1, from.y)

    (this, TestMoveAction(to))
  }
}
