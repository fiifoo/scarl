package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.action.test_assets.TestSleepActionDecider
import fi.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import fi.fiifoo.scarl.core.test_assets.TestCreatureFactory
import fi.fiifoo.scarl.core.{RealityBubble, State}
import fi.fiifoo.scarl.status.SleepStatus
import org.scalatest._

class SleepActionSpec extends FlatSpec with Matchers {

  "SleepAction" should "generate sleep status" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State()),
      TestSleepActionDecider
    )

    def s = bubble.s

    bubble.be()
    s.entities(ActiveStatusId(2)) should ===(SleepStatus(ActiveStatusId(2), 1, CreatureId(1)))
  }

  it should "heal creature" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = 2)),
      TestSleepActionDecider
    )

    def s = bubble.s

    bubble.be()
    bubble.be()
    CreatureId(1)(s).damage should ===(1)
  }

  it should "remove sleep status when fully healed" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = 2)),
      TestSleepActionDecider
    )

    def s = bubble.s

    bubble.be() // sleep action
    bubble.be() // heal
    bubble.be() // pass action
    s.entities.isDefinedAt(ActiveStatusId(2)) should ===(true)
    bubble.be() // heal
    s.entities.isDefinedAt(ActiveStatusId(2)) should ===(false)
  }
}
