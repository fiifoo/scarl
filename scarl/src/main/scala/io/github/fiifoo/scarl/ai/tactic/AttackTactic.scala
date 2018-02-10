package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{AttackIntention, ChargeIntention, PursueIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.SafeCreatureId
import io.github.fiifoo.scarl.core.geometry._

case class AttackTactic(target: SafeCreatureId, destination: Location) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    AttackIntention(target),
    Priority.top
  ), (
    ChargeIntention(target),
    Priority.high
  ), (
    PursueIntention(target, destination),
    Priority.medium
  ))
}