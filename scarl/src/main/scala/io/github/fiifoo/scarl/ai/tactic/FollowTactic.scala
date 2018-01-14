package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{FollowIntention, SeekEnemyIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.SafeCreatureId

case class FollowTactic(target: SafeCreatureId) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    SeekEnemyIntention,
    Priority.high
  ), (
    FollowIntention(target),
    Priority.high
  ))
}
