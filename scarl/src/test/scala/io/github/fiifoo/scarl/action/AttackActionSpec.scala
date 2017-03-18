package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.test_assets.TestAttackTactic
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import org.scalatest._

class AttackActionSpec extends FlatSpec with Matchers {

  "AttackAction" should "damage creatures" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State(), 2),
      TestAttackTactic
    )

    var damage1 = 0
    var damage2 = 0

    s = bubble(s)
    CreatureId(1)(s).damage should ===(damage1)
    CreatureId(2)(s).damage should be > damage2
    damage2 = CreatureId(2)(s).damage

    s = bubble(s)
    CreatureId(1)(s).damage should be > damage1
    CreatureId(2)(s).damage should ===(damage2)
    damage1 = CreatureId(1)(s).damage

    s = bubble(s)
    CreatureId(1)(s).damage should ===(damage1)
    CreatureId(2)(s).damage should be > damage2
  }

  it should "kill creature" in {
    val c1 = TestCreatureFactory.create(health = 1)
    val c2 = TestCreatureFactory.create(health = 1000)
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(TestCreatureFactory.generate(State(), 1, c1), 1, c2),
      TestAttackTactic
    )

    s = bubble(s) // damage to CreatureId(2)
    s.entities.size should ===(2)
    s = bubble(s) // damage to CreatureId(1) killing it
    s.entities.size should ===(1)
    s.entities.get(CreatureId(1)).isEmpty should ===(true)
    s.entities.get(CreatureId(2)).isEmpty should ===(false)
  }

}
