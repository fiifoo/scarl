package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State

class CombinedEffectListener(listeners: List[EffectListener]) extends EffectListener {
  def apply(s: State, effect: Effect): Unit = {
    listeners.foreach(_ (s, effect))
  }
}
