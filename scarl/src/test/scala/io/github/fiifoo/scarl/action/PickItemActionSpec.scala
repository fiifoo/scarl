package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestPickItemTactic
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestItemFactory}
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class PickItemActionSpec extends FlatSpec with Matchers {

  "PickItemAction" should "pick items" in {
    var s = TestItemFactory.generate(
      TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestPickItemTactic)),
      2,
      Location(0, 0)
    )

    ItemId(3)(s).container should ===(ContainerId(2))
    s = RealityBubble(s).get.state
    ItemId(3)(s).container should ===(CreatureId(1))

    ItemId(5)(s).container should ===(ContainerId(4))
    s = RealityBubble(s).get.state
    ItemId(5)(s).container should ===(CreatureId(1))

    CreatureId(1)(s).tick > 1 should ===(true)
  }
}
