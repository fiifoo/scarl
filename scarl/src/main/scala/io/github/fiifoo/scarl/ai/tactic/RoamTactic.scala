package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, CheckGreetIntention, CheckPartyCombatIntention}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object RoamTactic extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckAttackIntention(),
    Priority.high
  ), (
    CheckPartyCombatIntention,
    Priority.medium
  ), (
    CheckGreetIntention,
    Priority.low
  ))

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    apply(s, actor, random) getOrElse Utils.roam(s, actor, this, random)
  }
}
