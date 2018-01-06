package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.ai.tactic.PassTactic
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class PassActionSpec extends FlatSpec with Matchers {

  "PassAction" should "pass creature turn" in {
    var s = TestCreatureFactory.generate(State(), 2, TestCreatureFactory.create(behavior = PassTactic))

    s = RealityBubble(s).get.state
    s.cache.actorQueue.headOption.get should ===(CreatureId(2))

    s = RealityBubble(s).get.state
    s.cache.actorQueue.headOption.get should ===(CreatureId(1))
  }
}
