package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.State

case class SimulationState[T](instance: State,
                              result: T,
                              stopped: Boolean = false,
                             )
