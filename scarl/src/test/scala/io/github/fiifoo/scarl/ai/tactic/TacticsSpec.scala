package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.test_assets.TestPassTactic
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class TacticsSpec extends FlatSpec with Matchers {

  val bubble = new RealityBubble(
    TestCreatureFactory.generate(State(), 2, TestCreatureFactory.create(health = 100)),
    ai = (actor: CreatureId) => actor match {
      case CreatureId(1) => RoamTactic(CreatureId(1))
      case CreatureId(2) => TestPassTactic(CreatureId(2))
    }
  )

  def s = bubble.s

  "RoamTactic" should "move creature and continue roaming" in {
    bubble.s = LocatableLocationMutation(CreatureId(2), Location(1000, 0))(s) // off you go

    CreatureId(1)(s).location should ===(Location(0, 0))
    bubble.be()
    CreatureId(1)(s).location should ===(Location(-1, 0))
    s.tactics(CreatureId(1)) should ===(RoamTactic(CreatureId(1)))

    bubble.be() // other creature
  }

  it should "switch tactic to charge when enemy is close by" in {
    bubble.s = LocatableLocationMutation(CreatureId(1), Location(0, 0))(s) // reset
    bubble.s = LocatableLocationMutation(CreatureId(2), Location(2, 0))(s) // welcome back

    bubble.be()
    CreatureId(1)(s).location should ===(Location(1, 0))
    s.tactics(CreatureId(1)) should ===(ChargeTactic(CreatureId(1), SafeCreatureId(CreatureId(2)), Location(2, 0)))

    bubble.be() // other creature
  }

  "ChargeTactic" should "move creature towards other" in {
    // already did
    CreatureId(1)(s).location should ===(Location(1, 0))
  }

  it should "attack other creature when adjacent" in {
    CreatureId(2)(s).damage should ===(0)
    bubble.be()
    CreatureId(1)(s).location should ===(Location(1, 0))
    CreatureId(2)(s).damage should ===(1)

    bubble.be() // other creature
    bubble.be() // other creature, stupid attack cost
  }

  it should "switch tactic to pursue when enemy leaves field of vision" in {
    bubble.s = LocatableLocationMutation(CreatureId(2), Location(1000, 0))(s) // begone
    bubble.be()
    s.tactics(CreatureId(1)) should ===(PursueTactic(CreatureId(1), SafeCreatureId(CreatureId(2)), Location(2, 0)))

    bubble.be() // other creature
  }

  "PursueTactic" should "move creature to last known enemy position" in {
    // already did
    CreatureId(1)(s).location should ===(Location(2, 0))
  }

  it should "switch tactic back to roaming if enemy is not found" in {
    bubble.be()
    s.tactics(CreatureId(1)) should ===(RoamTactic(CreatureId(1)))

    bubble.be() // other creature
  }
}
