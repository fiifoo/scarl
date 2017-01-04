package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.{Action, ActionDecider}
import fi.fiifoo.scarl.core.entity.Creature

object TestActiveStatusActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    TestActiveStatusAction()
  }
}
