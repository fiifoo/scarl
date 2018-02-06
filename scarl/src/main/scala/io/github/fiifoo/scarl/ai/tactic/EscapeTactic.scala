package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.EscapeIntention
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.geometry.Location

case class EscapeTactic(source: Location, destination: Location) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = List((
    EscapeIntention(source, destination),
    Priority.medium
  ))
}
