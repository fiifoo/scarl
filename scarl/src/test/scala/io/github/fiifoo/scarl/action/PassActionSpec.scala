package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestPassActionDecider
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class PassActionSpec extends FlatSpec with Matchers {

  "PassAction" should "pass creature turn" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 2),
      TestPassActionDecider
    )

    bubble.be()
    bubble.actors.head should ===(CreatureId(2))

    bubble.be()
    bubble.actors.head should ===(CreatureId(1))
  }
}
