package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.creature.Missile.Guidance

object Missile {

  sealed trait Guidance

  case object Guided extends Guidance

  case object Smart extends Guidance

}

case class Missile(guidance: Option[Guidance] = None)
