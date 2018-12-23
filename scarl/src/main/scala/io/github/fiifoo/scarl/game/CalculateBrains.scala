package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.ai.Brain
import io.github.fiifoo.scarl.core.mutation.BrainsMutation
import io.github.fiifoo.scarl.world.SiteId

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object CalculateBrains {

  case class Calculation(calculations: Future[Iterable[Brain]], area: SiteId)

  def apply(state: RunState)(implicit ec: ExecutionContext): Calculation = {
    val calculations = state.instance.brains.values map (brain => {
      val (random, _) = state.instance.rng() // All brains will have same seed and state rng is not updated. Might be problem.

      Future {
        brain(state.instance, random)
      }
    })

    Calculation(Future.sequence(calculations), state.game.area)
  }

  def commit(state: RunState, calculation: Calculation): RunState = {
    if (state.game.area != calculation.area) {
      state
    } else {
      val result = Await.result(calculation.calculations, Duration.Inf)

      state.copy(
        instance = BrainsMutation((result map (brain => brain.faction -> brain)).toMap)(state.instance)
      )
    }
  }
}
