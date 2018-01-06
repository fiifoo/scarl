package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic
import io.github.fiifoo.scarl.core.entity.CreatureId

case class TacticMutation(actor: CreatureId, tactic: Tactic) extends Mutation {

  def apply(s: State): State = {
    s.copy(tactics = s.tactics + (actor -> tactic))
  }
}
