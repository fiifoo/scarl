package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.mutation.NewGoalMutation
import io.github.fiifoo.scarl.core.world.GoalId

case class AchieveGoalEffect(goal: GoalId,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(
      NewGoalMutation(goal)
    )
  }
}
