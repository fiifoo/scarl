package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.RemoveEntitiesMutation
import io.github.fiifoo.scarl.core.{RealityBubble, State}

import scala.annotation.tailrec

class Simulation[T](listener: SimulationListener[T],
                    ai: CreatureId => Tactic,
                    turnLimit: Int
                   ) {

  val turnTime: Int = 100
  val bubble = RealityBubble(ai)

  def apply(state: SimulationState[T], fixedAction: Option[Action] = None): SimulationState[T] = {
    val timeLimit = state.instance.tick + (turnLimit * turnTime)

    run(
      state.copy(instance = state.instance.copy(simulation = State.Simulation(running = true))),
      timeLimit,
      fixedAction
    )
  }

  @tailrec
  private def run(state: SimulationState[T], timeLimit: Int, fixedAction: Option[Action] = None): SimulationState[T] = {
    if (state.stopped || state.instance.tick >= timeLimit) {
      state
    } else {
      val next = bubble(state.instance, fixedAction) map (data => {
        val nextState = state.copy(instance = RemoveEntitiesMutation()(data.state))

        listener(nextState, data)
      }) getOrElse {
        state.copy(stopped = true)
      }

      run(next, timeLimit)
    }
  }
}
