package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getTargetStatuses

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
