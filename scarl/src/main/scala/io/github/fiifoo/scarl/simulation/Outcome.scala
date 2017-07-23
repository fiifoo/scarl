package io.github.fiifoo.scarl.simulation

sealed trait Outcome

object Outcome {

  case object Good extends Outcome

  case object Bad extends Outcome

  case object Neutral extends Outcome

}
