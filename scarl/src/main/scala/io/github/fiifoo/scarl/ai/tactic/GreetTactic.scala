package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.PassAction
import io.github.fiifoo.scarl.ai.intention.CheckGreetIntention
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object GreetTactic extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckGreetIntention,
    Priority.infinite
  ))

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    apply(s, actor, random) getOrElse(this, PassAction)
  }
}
