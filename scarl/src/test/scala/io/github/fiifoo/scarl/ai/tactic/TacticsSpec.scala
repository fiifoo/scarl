package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.test_assets.TestPassTactic
import io.github.fiifoo.scarl.core.entity.{CreatureId, Faction, FactionId, SafeCreatureId}
import io.github.fiifoo.scarl.core.mutation.{LocatableLocationMutation, NewFactionMutation}
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class TacticsSpec extends FlatSpec with Matchers {

  val faction = Faction(FactionId("people"), Set(FactionId("people")))

  val initial = NewFactionMutation(faction)(TestCreatureFactory.generate(
    s = State(),
    count = 2,
    prototype = TestCreatureFactory.create(health = 100, faction = faction.id)
  ))

  var (bubble, s: State) = RealityBubble(
    initial = initial,
    ai = (actor: CreatureId) => actor match {
      case CreatureId(1) => RoamTactic(CreatureId(1))
      case _ => TestPassTactic(CreatureId(2))
    }
  )

  "RoamTactic" should "move creature and continue roaming" in {
    s = LocatableLocationMutation(CreatureId(2), Location(1000, 0))(s) // off you go

    CreatureId(1)(s).location should ===(Location(0, 0))
    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(-1, 1))
    s.tactics(CreatureId(1)) should ===(RoamTactic(CreatureId(1)))

    s = bubble(s) // other creature
  }

  it should "switch tactic to charge when enemy is close by" in {
    s = LocatableLocationMutation(CreatureId(1), Location(0, 0))(s) // reset
    s = LocatableLocationMutation(CreatureId(2), Location(2, 0))(s) // welcome back

    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(1, 0))
    s.tactics(CreatureId(1)) should ===(ChargeTactic(CreatureId(1), SafeCreatureId(CreatureId(2)), Location(2, 0)))

    s = bubble(s) // other creature
  }

  "ChargeTactic" should "move creature towards other" in {
    // already did
    CreatureId(1)(s).location should ===(Location(1, 0))
  }

  it should "attack other creature when adjacent" in {
    CreatureId(2)(s).damage should ===(0)
    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(1, 0))
    CreatureId(2)(s).damage should be > 0

    s = bubble(s) // other creature
  }

  it should "switch tactic to pursue when enemy leaves field of vision" in {
    s = LocatableLocationMutation(CreatureId(2), Location(1000, 0))(s) // begone
    s = bubble(s)
    s.tactics(CreatureId(1)) should ===(PursueTactic(CreatureId(1), SafeCreatureId(CreatureId(2)), Location(2, 0)))

    s = bubble(s) // other creature
  }

  "PursueTactic" should "move creature to last known enemy position" in {
    // already did
    CreatureId(1)(s).location should ===(Location(2, 0))
  }

  it should "switch tactic back to roaming if enemy is not found" in {
    s = bubble(s)
    s.tactics(CreatureId(1)) should ===(RoamTactic(CreatureId(1)))

    s = bubble(s) // other creature
  }
}
