package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.world.GoalId

case class NewGoalMutation(goal: GoalId) extends Mutation {
  def apply(s: State): State = {
    s.copy(creature = s.creature.copy(
      goals = s.creature.goals + goal
    ))
  }
}
