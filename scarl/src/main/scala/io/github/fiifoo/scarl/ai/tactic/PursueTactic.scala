package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, PursueIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.SafeCreatureId
import io.github.fiifoo.scarl.core.geometry.Location

case class PursueTactic(target: SafeCreatureId, destination: Location) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckAttackIntention(),
    Priority.high
  ), (
    PursueIntention(target, destination),
    Priority.medium
  ))
}
