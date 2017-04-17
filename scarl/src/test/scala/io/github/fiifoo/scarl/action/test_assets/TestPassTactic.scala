package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case class TestPassTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, random: Random): (Tactic, Action) = {
    (this, PassAction())
  }
}
