package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, CheckPartyCombatIntention, TravelIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.geometry.Location

case class TravelTactic(destination: Location,
                        priority: Priority.Value = Priority.low,
                        waiting: Boolean = false
                       ) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckAttackIntention(),
    Priority.high
  ), (
    CheckPartyCombatIntention,
    Priority.medium
  ),(
    TravelIntention(destination, priority, waiting),
    priority
  ))
}
