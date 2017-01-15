package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.Creature

object TestActiveStatusActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    TestActiveStatusAction()
  }
}
