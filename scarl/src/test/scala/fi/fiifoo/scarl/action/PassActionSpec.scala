package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.action.test_assets.TestPassActionDecider
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.test_assets.TestCreatureFactory
import fi.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class PassActionSpec extends FlatSpec with Matchers {

  "PassAction" should "pass creature turn" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 2),
      TestPassActionDecider
    )

    bubble.be()
    bubble.actorQueue.head should ===(CreatureId(2))

    bubble.be()
    bubble.actorQueue.head should ===(CreatureId(1))
  }
}
