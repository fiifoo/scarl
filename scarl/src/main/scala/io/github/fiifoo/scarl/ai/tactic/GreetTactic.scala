package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.ai.Greeting
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object GreetTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val action = Greeting(s, actor) getOrElse PassAction

    (this, action)
  }
}
