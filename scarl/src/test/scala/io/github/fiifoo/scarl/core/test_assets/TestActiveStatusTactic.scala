package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case class TestActiveStatusTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, random: Random): (Tactic, Action) = {
    (this, TestActiveStatusAction())
  }
}
