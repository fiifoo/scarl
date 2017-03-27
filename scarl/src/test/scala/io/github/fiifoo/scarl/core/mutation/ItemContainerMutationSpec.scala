package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestItemFactory}
import org.scalatest._

class ItemContainerMutationSpec extends FlatSpec with Matchers {

  "ItemContainerMutation" should "mutate item" in {
    val container = CreatureId(5)
    val initial = TestItemFactory.generate(State(), 1, CreatureId(0))
    val item = ItemId(1)

    val mutated = ItemContainerMutation(item, container)(initial)
    item(mutated).container should ===(container)
  }

  it should "mutate index" in {
    val initial = TestCreatureFactory.generate(TestItemFactory.generate(State(), 4, CreatureId(0)), 2)
    val item1 = ItemId(1)
    val item2 = ItemId(2)
    val item3 = ItemId(3)
    val item4 = ItemId(4)
    val container1 = CreatureId(5)
    val container2 = CreatureId(6)

    val mutation1 = ItemContainerMutation(item1, container1)
    val mutation2 = ItemContainerMutation(item2, container2)
    val mutation3 = ItemContainerMutation(item3, container2)
    val mutation4 = ItemContainerMutation(item4, container2)
    val mutation5 = ItemContainerMutation(item4, container1)

    val mutated = mutation5(mutation4(mutation3(mutation2(mutation1(initial)))))
    val should = Map(
      container1 -> Set(item4, item1),
      container2 -> Set(item3, item2)
    )

    mutated.index.containerItems should ===(should)
  }

}
