package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.{Location, State}

trait Effect {
  val parent: Option[Effect]

  def apply(s: State): EffectResult
}

trait LocalizedDescriptionEffect extends Effect {
  val location: Location
  val description: Option[String]
}
