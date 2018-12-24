package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State

case class ResetGoalsMutation() extends Mutation {
  def apply(s: State): State = {
    s.copy(creature = s.creature.copy(
      goals = Set()
    ))
  }
}
