package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.{SleepStatus, TestSleepTactic}
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.RemoveEntitiesMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class SleepActionSpec extends FlatSpec with Matchers {

  "SleepAction" should "generate sleep status" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestSleepTactic))

    s = RealityBubble(s).get.state
    s.entities(ActiveStatusId(2)) should ===(SleepStatus(ActiveStatusId(2), 1, CreatureId(1)))
  }

  it should "heal creature" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = 2, behavior = TestSleepTactic))

    s = RealityBubble(s).get.state
    s = RealityBubble(s).get.state
    CreatureId(1)(s).damage should ===(1)
  }

  it should "remove sleep status when fully healed" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = 2, behavior = TestSleepTactic))

    s = RealityBubble(s).get.state // sleep action
    s = RealityBubble(s).get.state // heal
    s = RealityBubble(s).get.state // pass action
    s.entities.isDefinedAt(ActiveStatusId(2)) should ===(true)
    s = RealityBubble(s).get.state // heal
    s = RemoveEntitiesMutation()(s)
    s.entities.isDefinedAt(ActiveStatusId(2)) should ===(false)
  }
}
