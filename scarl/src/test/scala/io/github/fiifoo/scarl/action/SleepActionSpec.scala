package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestSleepTactic
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import io.github.fiifoo.scarl.status.SleepStatus
import org.scalatest._

class SleepActionSpec extends FlatSpec with Matchers {

  "SleepAction" should "generate sleep status" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State()),
      TestSleepTactic
    )

    s = bubble(s)
    s.entities(ActiveStatusId(2)) should ===(SleepStatus(ActiveStatusId(2), 1, CreatureId(1)))
  }

  it should "heal creature" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = 2)),
      TestSleepTactic
    )

    s = bubble(s)
    s = bubble(s)
    CreatureId(1)(s).damage should ===(1)
  }

  it should "remove sleep status when fully healed" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = 2)),
      TestSleepTactic
    )

    s = bubble(s) // sleep action
    s = bubble(s) // heal
    s = bubble(s) // pass action
    s.entities.isDefinedAt(ActiveStatusId(2)) should ===(true)
    s = bubble(s) // heal
    s.entities.isDefinedAt(ActiveStatusId(2)) should ===(false)
  }
}
