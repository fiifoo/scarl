package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Tactic

case class TacticMutation(tactic: Tactic) extends Mutation {

  def apply(s: State): State = {
    s.copy(tactics = s.tactics + (tactic.actor -> tactic))
  }
}
