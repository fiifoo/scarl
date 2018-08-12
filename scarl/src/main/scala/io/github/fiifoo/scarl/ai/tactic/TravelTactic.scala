package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{CheckAttackIntention, TravelIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.geometry.Location

case class TravelTactic(destination: Location, waiting: Boolean = false) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    CheckAttackIntention(),
    Priority.high
  ), (
    TravelIntention(destination, waiting),
    Priority.medium
  ))
}
