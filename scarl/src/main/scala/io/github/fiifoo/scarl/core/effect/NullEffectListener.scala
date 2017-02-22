package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State

object NullEffectListener extends EffectListener {
  def apply(s: State, effect: Effect): Unit = {}
}
