package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.{SeekEnemyIntention, TravelIntention}
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

case class TravelTactic(destination: Waypoint) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    SeekEnemyIntention,
    Priority.high
  ), (
    TravelIntention(destination),
    Priority.medium
  ))
}
