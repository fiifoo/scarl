package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.{RemovableEntityMutation, RemoveEntitiesMutation}
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestActiveStatusTactic, TestCreatureFactory, TestMoveTactic}
import org.scalatest._

class RealityBubbleSpec extends FlatSpec with Matchers {

  "RealityBubble" should "move a creature" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State()),
      TestMoveTactic
    )

    s = bubble(s)

    CreatureId(1)(s).location should ===(Location(1, 0))
  }

  it should "add new actor to actor queue" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State()),
      TestActiveStatusTactic
    )

    s.entities.size should ===(1)
    s = bubble(s)
    s.entities.size should ===(2)
    s.entities(ActiveStatusId(2)) should ===(TestActiveStatus(ActiveStatusId(2), 1, CreatureId(1)))
    s.tmp.addedActors.size should ===(0)

    bubble.nextActor.get should ===(ActiveStatusId(2))
  }

  it should "activate new actor correctly" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State()),
      TestActiveStatusTactic
    )

    CreatureId(1)(s).damage should ===(0)
    s = bubble(s) // actor status is created
    s = bubble(s) // actor status is activated
    CreatureId(1)(s).damage should ===(1)
  }

  it should "have correct order for actor queue" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State()),
      TestActiveStatusTactic
    )

    // creature acts first time
    bubble.nextActor.get should ===(CreatureId(1))
    s = bubble(s)
    s.tick should ===(1)

    // actor status 1 is activated first time
    bubble.nextActor.get should ===(ActiveStatusId(2))
    s = bubble(s)
    s.tick should ===(1)

    // actor status 1 is activated second time
    bubble.nextActor.get should ===(ActiveStatusId(2))
    s = bubble(s)
    s.tick should ===(51)

    // creature acts second time
    bubble.nextActor.get should ===(CreatureId(1))
    s = bubble(s)
    s.tick should ===(101)

    // actor status 1 is activated third time
    bubble.nextActor.get should ===(ActiveStatusId(2))
    s = bubble(s)
    s.tick should ===(101)

    // actor status 2 is activated first time
    bubble.nextActor.get should ===(ActiveStatusId(3))
    s = bubble(s)
    s.tick should ===(101)

    // actor status 1 is activated fourth time
    bubble.nextActor.get should ===(ActiveStatusId(2))
    s = bubble(s)
    s.tick should ===(151)

    // actor status 2 is activated second time
    bubble.nextActor.get should ===(ActiveStatusId(3))
    s = bubble(s)
    s.tick should ===(151)

    // creature acts third time
    bubble.nextActor.get should ===(CreatureId(1))
    s = bubble(s)
    s.tick should ===(201)

    CreatureId(1)(s).damage should ===(6)
  }

  it should "ignore removed actors" in {
    var (bubble, s) = RealityBubble(
      TestCreatureFactory.generate(State(), 3),
      TestMoveTactic
    )

    s = bubble(s)
    s = bubble(s)
    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(1, 0))
    CreatureId(2)(s).location should ===(Location(1, 0))
    CreatureId(3)(s).location should ===(Location(1, 0))

    s = RemoveEntitiesMutation()(RemovableEntityMutation(CreatureId(2))(s))

    s = bubble(s)
    s = bubble(s)
    s = bubble(s)
    CreatureId(1)(s).location should ===(Location(2, 0))
    CreatureId(3)(s).location should ===(Location(2, 0))
  }

}
