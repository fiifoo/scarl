package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.{RemovableEntityMutation, RemoveEntitiesMutation}
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestActiveStatusActionDecider, TestCreatureFactory, TestMoveActionDecider}
import org.scalatest._

class RealityBubbleSpec extends FlatSpec with Matchers {

  "RealityBubble" should "move a creature" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State()),
      TestMoveActionDecider
    )

    def s = bubble.s

    bubble.be()

    CreatureId(1)(s).location should ===(Location(1, 0))
  }

  it should "add new actor to actor queue" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State()),
      TestActiveStatusActionDecider
    )

    def s = bubble.s

    s.entities.size should ===(1)
    bubble.be()
    s.entities.size should ===(2)
    s.entities(ActiveStatusId(2)) should ===(TestActiveStatus(ActiveStatusId(2), 1, CreatureId(1)))
    s.tmp.addedActors.size should ===(0)

    bubble.actors.head should ===(ActiveStatusId(2))
  }

  it should "activate new actor correctly" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State()),
      TestActiveStatusActionDecider
    )

    def s = bubble.s

    CreatureId(1)(s).damage should ===(0)
    bubble.be() // actor status is created
    bubble.be() // actor status is activated
    CreatureId(1)(s).damage should ===(1)
  }

  it should "have correct order for actor queue" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State()),
      TestActiveStatusActionDecider
    )

    def s = bubble.s

    // creature acts first time
    bubble.actors.head should ===(CreatureId(1))
    bubble.be()
    s.tick should ===(1)

    // actor status 1 is activated first time
    bubble.actors.head should ===(ActiveStatusId(2))
    bubble.be()
    s.tick should ===(1)

    // actor status 1 is activated second time
    bubble.actors.head should ===(ActiveStatusId(2))
    bubble.be()
    s.tick should ===(51)

    // creature acts second time
    bubble.actors.head should ===(CreatureId(1))
    bubble.be()
    s.tick should ===(101)

    // actor status 1 is activated third time
    bubble.actors.head should ===(ActiveStatusId(2))
    bubble.be()
    s.tick should ===(101)

    // actor status 2 is activated first time
    bubble.actors.head should ===(ActiveStatusId(3))
    bubble.be()
    s.tick should ===(101)

    // actor status 1 is activated fourth time
    bubble.actors.head should ===(ActiveStatusId(2))
    bubble.be()
    s.tick should ===(151)

    // actor status 2 is activated second time
    bubble.actors.head should ===(ActiveStatusId(3))
    bubble.be()
    s.tick should ===(151)

    // creature acts third time
    bubble.actors.head should ===(CreatureId(1))
    bubble.be()
    s.tick should ===(201)

    CreatureId(1)(s).damage should ===(6)
  }

  it should "ignore removed actors" in {
    val bubble = new RealityBubble(
      TestCreatureFactory.generate(State(), 3),
      TestMoveActionDecider
    )

    def s = bubble.s

    bubble.be()
    bubble.be()
    bubble.be()
    CreatureId(1)(s).location should ===(Location(1, 0))
    CreatureId(2)(s).location should ===(Location(1, 0))
    CreatureId(3)(s).location should ===(Location(1, 0))

    bubble.s = RemoveEntitiesMutation()(RemovableEntityMutation(CreatureId(2))(s))

    bubble.be()
    bubble.be()
    bubble.be()
    CreatureId(1)(s).location should ===(Location(2, 0))
    CreatureId(3)(s).location should ===(Location(2, 0))
  }

}
