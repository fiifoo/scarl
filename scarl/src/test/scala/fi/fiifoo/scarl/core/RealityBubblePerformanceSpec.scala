package fi.fiifoo.scarl.core

import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.mutation.LocatableLocationMutation
import fi.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestMoveActionDecider}
import org.scalatest._

class RealityBubblePerformanceSpec extends FlatSpec with Matchers {
  val count = 10000
  val bubble = new RealityBubble(
    TestCreatureFactory.generate(State(), count),
    TestMoveActionDecider
  )
  def s = bubble.s

  "RealityBubble" should "initialize for performance test" in {
    for (i <- 1 to count) {
      val location = Location(0, (i - 1) / 10) // 10 entities in every location
      bubble.s = LocatableLocationMutation(CreatureId(i), location)(s)
    }
  }

  it should "move a lot of creatures" in {
    for (i <- 1 to count + 1) bubble.be()

    CreatureId(1)(s).location.x should ===(2)
    CreatureId(count)(s).location.x should ===(1)
  }
}
