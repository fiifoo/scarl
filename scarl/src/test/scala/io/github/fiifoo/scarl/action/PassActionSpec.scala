package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestPassTactic
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class PassActionSpec extends FlatSpec with Matchers {

  "PassAction" should "pass creature turn" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State(), 2),
      TestPassTactic
    )

    s = bubble(s)
    bubble.nextActor.get should ===(CreatureId(2))

    s = bubble(s)
    bubble.nextActor.get should ===(CreatureId(1))
  }
}
