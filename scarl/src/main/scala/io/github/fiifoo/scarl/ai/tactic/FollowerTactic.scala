package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{FollowerIntention, SeekEnemyIntention}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority}
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object FollowerTactic extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List((
    SeekEnemyIntention,
    Priority.high
  ), (
    FollowerIntention,
    Priority.high
  ))

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    apply(s, actor, random) getOrElse Utils.roam(s, actor, this, random)
  }
}
