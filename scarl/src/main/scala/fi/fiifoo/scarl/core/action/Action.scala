package fi.fiifoo.scarl.core.action

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity.Creature

trait Action {
  def apply(s: State, actor: Creature): List[Effect]
}
