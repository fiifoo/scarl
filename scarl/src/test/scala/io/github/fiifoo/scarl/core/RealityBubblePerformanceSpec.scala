package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestMoveTactic}
import org.scalatest._

class RealityBubblePerformanceSpec extends FlatSpec with Matchers {
  val count = 10000
  var s = TestCreatureFactory.generate(State(), count, TestCreatureFactory.create(behavior = TestMoveTactic))

  "RealityBubble" should "initialize for performance test" in {
    for (i <- 1 to count) {
      val location = Location(0, (i - 1) / 10) // 10 entities in every location
      s = LocatableLocationMutation(CreatureId(i), location)(s)
    }
  }

  it should "move a lot of creatures" in {
    for (_ <- 1 to count + 1) s = RealityBubble(s).get.state

    CreatureId(1)(s).location.x should ===(2)
    CreatureId(count)(s).location.x should ===(1)
  }
}
