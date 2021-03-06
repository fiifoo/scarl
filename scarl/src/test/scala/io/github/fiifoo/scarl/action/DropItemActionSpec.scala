package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestDropItemTactic
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestItemFactory}
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class DropItemActionSpec extends FlatSpec with Matchers {

  "DropItemAction" should "drop items" in {
    val location = Location(2, 3)

    val creaturePrototype = TestCreatureFactory.create(location = location, behavior = TestDropItemTactic)

    var s = TestItemFactory.generate(TestCreatureFactory.generate(State(), 1, creaturePrototype), 2, CreatureId(1))

    ItemId(2)(s).container should ===(CreatureId(1))
    CreatureId(1)(s).location should ===(location)
    s = RealityBubble(s).get.state
    ItemId(2)(s).container should ===(ContainerId(4))
    ContainerId(4)(s).location should ===(location)

    ItemId(3)(s).container should ===(CreatureId(1))
    CreatureId(1)(s).location should ===(location)
    s = RealityBubble(s).get.state
    ItemId(3)(s).container should ===(ContainerId(5))
    ContainerId(5)(s).location should ===(location)

    CreatureId(1)(s).tick > 1 should ===(true)
  }
}
