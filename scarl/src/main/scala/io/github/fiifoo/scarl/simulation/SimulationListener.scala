package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.RealityBubble

trait SimulationListener[T] {
  def apply(state: SimulationState[T], data: RealityBubble.Result): SimulationState[T]
}
