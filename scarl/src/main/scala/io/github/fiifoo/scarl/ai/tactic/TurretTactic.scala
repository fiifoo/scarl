package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, CheckGreetIntention, PassIntention}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object TurretTactic extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckAttackIntention(enableMove = false),
    Priority.top
  ), (
    CheckGreetIntention,
    Priority.high
  ), (
    PassIntention,
    Priority.high
  ))

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    apply(s, actor, random).get
  }
}
