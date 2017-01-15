package fi.fiifoo.scarl.ai

import fi.fiifoo.scarl.action.PassAction
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.{Action, ActionDecider => ActionDeciderTrait}
import fi.fiifoo.scarl.core.entity.Creature

object ActionDecider extends ActionDeciderTrait {

  def apply(s: State, actor: Creature): Action = PassAction()
}
