package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CreatureTacticMutation(actor: CreatureId, tactic: Option[Tactic]) extends Mutation {

  def apply(s: State): State = {
    val next = tactic map (tactic => {
      s.creature.tactics + (actor -> tactic)
    }) getOrElse {
      s.creature.tactics - actor
    }

    s.copy(creature = s.creature.copy(
      tactics = next
    ))
  }
}
