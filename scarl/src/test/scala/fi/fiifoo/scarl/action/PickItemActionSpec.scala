package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.action.test_assets.TestPickItemActionDecider
import fi.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import fi.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestItemFactory}
import fi.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class PickItemActionSpec extends FlatSpec with Matchers {

  "PickItemAction" should "pick items" in {
    val bubble = new RealityBubble(
      TestItemFactory.generate(TestCreatureFactory.generate(State(), 1), 2, Location(0, 0)),
      TestPickItemActionDecider
    )

    def s = bubble.s

    ItemId(3)(s).container should ===(ContainerId(2))
    bubble.be()
    ItemId(3)(s).container should ===(CreatureId(1))

    ItemId(5)(s).container should ===(ContainerId(4))
    bubble.be()
    ItemId(5)(s).container should ===(CreatureId(1))

    CreatureId(1)(s).tick > 1 should ===(true)
  }
}
