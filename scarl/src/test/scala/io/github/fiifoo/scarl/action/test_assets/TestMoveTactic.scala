package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

case class TestMoveTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, random: Random): (Tactic, Action) = {
    val from = actor(s).location
    val to = Location(from.x + 1, from.y + 1)

    (this, MoveAction(to))
  }
}
