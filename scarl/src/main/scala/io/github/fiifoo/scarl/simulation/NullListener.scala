package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.RealityBubble

object NullListener extends SimulationListener[Null] {
  def apply(state: SimulationState[Null], data: RealityBubble.Result): SimulationState[Null] = {
    state
  }
}
