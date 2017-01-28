package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class TestActiveStatusTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    (this, TestActiveStatusAction())
  }
}
