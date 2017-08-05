package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.core.action.PassTactic
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.mutation.{LocatableLocationMutation, NewFactionMutation}
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{Location, RealityBubble, State}
import org.scalatest._

class TacticsSpec extends FlatSpec with Matchers {

  val faction = Faction(FactionId("people"), Set(FactionId("people")))

  val initial = NewFactionMutation(faction)(
    TestCreatureFactory.generate(
      TestCreatureFactory.generate(
        State(),
        1,
        TestCreatureFactory.create(health = 100, faction = faction.id, behavior = RoamTactic)
      ),
      1,
      TestCreatureFactory.create(health = 100, faction = faction.id, behavior = PassTactic))
  )

  var s: State = initial

  "RoamTactic" should "move creature and continue roaming" in {
    s = LocatableLocationMutation(CreatureId(2), Location(1000, 0))(s) // off you go

    CreatureId(1)(s).location should ===(Location(0, 0))
    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(-1, 1))
    s.tactics(CreatureId(1)) should ===(RoamTactic)

    s = RealityBubble(s).get.state // other creature
  }

  it should "switch tactic to charge when enemy is close by" in {
    s = LocatableLocationMutation(CreatureId(1), Location(0, 0))(s) // reset
    s = LocatableLocationMutation(CreatureId(2), Location(2, 0))(s) // welcome back

    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(1, 0))
    s.tactics(CreatureId(1)) should ===(ChargeTactic(SafeCreatureId(CreatureId(2)), Location(2, 0)))

    s = RealityBubble(s).get.state // other creature
  }

  "ChargeTactic" should "move creature towards other" in {
    // already did
    CreatureId(1)(s).location should ===(Location(1, 0))
  }

  it should "attack other creature when adjacent" in {
    CreatureId(2)(s).damage should ===(0)
    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(1, 0))
    CreatureId(2)(s).damage should be > 0

    s = RealityBubble(s).get.state // other creature
  }

  it should "switch tactic to pursue when enemy leaves field of vision" in {
    s = LocatableLocationMutation(CreatureId(2), Location(1000, 0))(s) // begone
    s = RealityBubble(s).get.state
    s.tactics(CreatureId(1)) should ===(PursueTactic(SafeCreatureId(CreatureId(2)), Location(2, 0)))

    s = RealityBubble(s).get.state // other creature
  }

  "PursueTactic" should "move creature to last known enemy position" in {
    // already did
    CreatureId(1)(s).location should ===(Location(2, 0))
  }

  it should "switch tactic back to roaming if enemy is not found" in {
    s = RealityBubble(s).get.state
    s.tactics(CreatureId(1)) should ===(RoamTactic)

    s = RealityBubble(s).get.state // other creature
  }
}
