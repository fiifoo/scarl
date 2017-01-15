package fi.fiifoo.scarl.core.action

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.entity.Creature

trait ActionDecider {
  def apply(s: State, actor: Creature): Action
}
