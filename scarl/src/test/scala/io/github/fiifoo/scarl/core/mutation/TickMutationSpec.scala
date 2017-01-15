package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import org.scalatest._

class TickMutationSpec extends FlatSpec with Matchers {

  "TickMutation" should "mutate correctly" in {
    val tick = 5
    val initial = State()

    val mutated = TickMutation(tick)(initial)
    mutated.tick should ===(tick)
  }

}
