package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.core.Selectors.getTargetStatuses
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.action.{Behavior, PassAction}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object TestSleepTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val action = if (getTargetStatuses(s)(actor) exists (_ (s).isInstanceOf[SleepStatus])) {
      PassAction
    } else {
      SleepAction
    }

    (this, action)
  }
}
