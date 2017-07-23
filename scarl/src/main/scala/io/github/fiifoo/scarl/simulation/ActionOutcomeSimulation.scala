package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action

object ActionOutcomeSimulation {

  def apply(instance: State, simulation: Simulation[Outcome], action: Action): Outcome = {
    val state = createState(instance)

    simulation(state, Some(action)).result
  }

  private def createState(instance: State): SimulationState[Outcome] = {
    SimulationState(instance, Outcome.Neutral)
  }
}
