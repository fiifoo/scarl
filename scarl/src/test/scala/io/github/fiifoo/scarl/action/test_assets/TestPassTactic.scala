package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class TestPassTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    (this, PassAction())
  }
}
