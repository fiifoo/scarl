package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestAttackTactic
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class AttackActionSpec extends FlatSpec with Matchers {

  "AttackAction" should "damage creatures" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 2),
      TestAttackTactic
    )

    def s = bubble.s

    bubble.be()
    CreatureId(2)(s).damage should ===(1)

    bubble.be()
    CreatureId(1)(s).damage should ===(0) // missed...

    bubble.be()
    CreatureId(2)(s).damage should ===(2)
  }

  it should "kill creature" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 2, TestCreatureFactory.create(health = 2)),
      TestAttackTactic
    )

    def s = bubble.s

    bubble.be()
    bubble.be()
    s.entities.size should ===(2)
    s.nextEntityId should ===(3)
    s.entities.get(CreatureId(2)).isEmpty should ===(false)

    bubble.be()
    s.entities.size should ===(1)
    s.nextEntityId should ===(4) // death status was generated and immediately removed
    s.entities.get(CreatureId(2)).isEmpty should ===(true)
  }

}
