package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestMoveTactic
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class MoveActionSpec extends FlatSpec with Matchers {

  "MoveAction" should "move creatures" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State()),
      TestMoveTactic
    )

    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(1, 1))

    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(2, 2))
  }
}
