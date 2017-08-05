package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestMoveTactic}
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class MoveActionSpec extends FlatSpec with Matchers {

  "MoveAction" should "move creatures" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestMoveTactic))

    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(1, 0))

    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(2, 0))
  }
}
