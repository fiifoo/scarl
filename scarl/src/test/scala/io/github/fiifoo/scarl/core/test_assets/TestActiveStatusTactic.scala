package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object TestActiveStatusTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    (this, TestActiveStatusAction())
  }
}
