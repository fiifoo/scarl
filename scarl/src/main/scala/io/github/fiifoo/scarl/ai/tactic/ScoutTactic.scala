package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, CheckEscapeIntention, ScoutIntention}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

case class ScoutTactic(destination: Option[Location] = None, waiting: Boolean = false) extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckEscapeIntention,
    Priority.high
  ), (
    CheckAttackIntention(),
    Priority.high
  ), (
    ScoutIntention(destination, waiting),
    Priority.medium
  ))

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    apply(s, actor, random) getOrElse Utils.roam(s, actor, ScoutTactic(), random)
  }
}
