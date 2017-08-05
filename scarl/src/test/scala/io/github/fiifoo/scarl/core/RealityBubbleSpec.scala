package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.{RemovableEntityMutation, RemoveEntitiesMutation}
import io.github.fiifoo.scarl.core.test_assets._
import org.scalatest._

class RealityBubbleSpec extends FlatSpec with Matchers {

  "RealityBubble" should "move a creature" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestMoveTactic))

    s = RealityBubble(s).get.state

    CreatureId(1)(s).location should ===(Location(1, 0))
  }

  it should "add new actor to actor queue" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestActiveStatusTactic))

    s.entities.size should ===(1)
    s = RealityBubble(s).get.state
    s.entities.size should ===(2)
    s.entities(ActiveStatusId(2)) should ===(TestActiveStatus(ActiveStatusId(2), 1, CreatureId(1)))

    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(2))
  }

  it should "activate new actor correctly" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestActiveStatusTactic))

    CreatureId(1)(s).damage should ===(0)
    s = RealityBubble(s).get.state // actor status is created
    s = RealityBubble(s).get.state // actor status is activated
    CreatureId(1)(s).damage should ===(1)
  }

  it should "have correct order for actor queue" in {
    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(behavior = TestActiveStatusTactic))

    // creature acts first time
    s.cache.actorQueue.headOption.get should ===(CreatureId(1))
    s = RealityBubble(s).get.state
    s.tick should ===(1)

    // actor status 1 is activated first time
    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(2))
    s = RealityBubble(s).get.state
    s.tick should ===(1)

    // actor status 1 is activated second time
    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(2))
    s = RealityBubble(s).get.state
    s.tick should ===(51)

    // creature acts second time
    s.cache.actorQueue.headOption.get should ===(CreatureId(1))
    s = RealityBubble(s).get.state
    s.tick should ===(101)

    // actor status 1 is activated third time
    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(2))
    s = RealityBubble(s).get.state
    s.tick should ===(101)

    // actor status 2 is activated first time
    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(3))
    s = RealityBubble(s).get.state
    s.tick should ===(101)

    // actor status 1 is activated fourth time
    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(2))
    s = RealityBubble(s).get.state
    s.tick should ===(151)

    // actor status 2 is activated second time
    s.cache.actorQueue.headOption.get should ===(ActiveStatusId(3))
    s = RealityBubble(s).get.state
    s.tick should ===(151)

    // creature acts third time
    s.cache.actorQueue.headOption.get should ===(CreatureId(1))
    s = RealityBubble(s).get.state
    s.tick should ===(201)

    CreatureId(1)(s).damage should ===(6)
  }

  it should "ignore removed actors" in {
    var s = TestCreatureFactory.generate(State(), 3, TestCreatureFactory.create(behavior = TestMoveTactic))

    s = RealityBubble(s).get.state
    s = RealityBubble(s).get.state
    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(1, 0))
    CreatureId(2)(s).location should ===(Location(1, 0))
    CreatureId(3)(s).location should ===(Location(1, 0))

    s = RemovableEntityMutation(CreatureId(2))(s)
    s = RemoveEntitiesMutation()(s)

    s = RealityBubble(s).get.state
    s = RealityBubble(s).get.state
    CreatureId(1)(s).location should ===(Location(2, 0))
    CreatureId(3)(s).location should ===(Location(2, 0))
  }

}
