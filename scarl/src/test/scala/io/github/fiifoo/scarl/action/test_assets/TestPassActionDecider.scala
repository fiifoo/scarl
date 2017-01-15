package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.Creature

object TestPassActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    PassAction()
  }
}
