package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class ActorTickMutationSpec extends FlatSpec with Matchers {

  "ActorTickMutation" should "mutate correctly" in {
    val tick = 5
    val initial = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val mutated = ActorTickMutation(creature, tick)(initial)
    creature(mutated).tick should ===(tick)
  }

}
