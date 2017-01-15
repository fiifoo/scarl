package fi.fiifoo.scarl.action.test_assets

import fi.fiifoo.scarl.action.{PassAction, SleepAction}
import fi.fiifoo.scarl.core.Selectors.getTargetStatuses
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.{Action, ActionDecider}
import fi.fiifoo.scarl.core.entity.Creature
import fi.fiifoo.scarl.status.SleepStatus

object TestSleepActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    if (getTargetStatuses(actor.id)(s) exists (_.isInstanceOf[SleepStatus])) {
      PassAction()
    } else {
      SleepAction()
    }
  }
}
