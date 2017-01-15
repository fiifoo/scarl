package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.test_assets.TestCreatureFactory
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
