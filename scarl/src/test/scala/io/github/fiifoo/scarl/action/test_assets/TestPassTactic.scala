package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Rng, State}

case class TestPassTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    (this, PassAction(), rng)
  }
}
