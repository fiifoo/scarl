package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Rng, State}

case class TestActiveStatusTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    (this, TestActiveStatusAction(), rng)
  }
}
