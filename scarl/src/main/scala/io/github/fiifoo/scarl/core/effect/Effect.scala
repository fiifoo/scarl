package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State

trait Effect {
  val parent: Option[Effect]

  def apply(s: State): EffectResult
}
