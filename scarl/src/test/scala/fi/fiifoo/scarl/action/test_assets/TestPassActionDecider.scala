package fi.fiifoo.scarl.action.test_assets

import fi.fiifoo.scarl.action.PassAction
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.{Action, ActionDecider}
import fi.fiifoo.scarl.core.entity.Creature

object TestPassActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    PassAction()
  }
}
