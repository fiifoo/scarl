package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider => ActionDeciderTrait}
import io.github.fiifoo.scarl.core.entity.Creature

object ActionDecider extends ActionDeciderTrait {

  def apply(s: State, actor: Creature): Action = PassAction()
}
