package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State

trait EffectListener {
  def apply(s: State, effect: Effect): Unit
}
