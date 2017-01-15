package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.{PassAction, SleepAction}
import io.github.fiifoo.scarl.core.Selectors.getTargetStatuses
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.status.SleepStatus

object TestSleepActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    if (getTargetStatuses(actor.id)(s) exists (_.isInstanceOf[SleepStatus])) {
      PassAction()
    } else {
      SleepAction()
    }
  }
}
