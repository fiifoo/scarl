package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, FollowIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.SafeCreatureId

case class FollowTactic(target: SafeCreatureId) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckAttackIntention,
    Priority.high
  ), (
    FollowIntention(target),
    Priority.high
  ))
}
