package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.action.test_assets.TestMoveActionDecider
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.test_assets.TestCreatureFactory
import fi.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class MoveActionSpec extends FlatSpec with Matchers {

  "MoveAction" should "move creatures" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State()),
      TestMoveActionDecider
    )

    def s = bubble.s

    bubble.be()
    CreatureId(1)(s).location should ===(Location(1, 1))

    bubble.be()
    CreatureId(1)(s).location should ===(Location(2, 2))
  }
}
