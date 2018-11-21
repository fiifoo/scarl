package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.geometry.Location

object Signal {

  sealed trait Kind

  case object ConduitSignal extends Kind

  case object CreatureSignal extends Kind

  case object NoiseSignal extends Kind

  val Weak = 20
  val Medium = 40
  val Strong = 60

  val FuzzyRadius = 5
}

case class Signal(kind: Signal.Kind,
                  location: Location,
                  strength: Int,
                  radius: Int = 0,
                  owner: Option[FactionId] = None,
                  seed: Int,
                  tick: Int
                 )
