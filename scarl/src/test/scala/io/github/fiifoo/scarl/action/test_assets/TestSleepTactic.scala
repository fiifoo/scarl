package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.{PassAction, SleepAction}
import io.github.fiifoo.scarl.core.Selectors.getTargetStatuses
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Rng, State}
import io.github.fiifoo.scarl.status.SleepStatus

case class TestSleepTactic(actor: CreatureId) extends Tactic {

  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    val action = if (getTargetStatuses(s)(actor) exists (_ (s).isInstanceOf[SleepStatus])) {
      PassAction()
    } else {
      SleepAction()
    }

    (this, action, rng)
  }
}
