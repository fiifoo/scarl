package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory, TestItemFactory}
import org.scalatest._

class RemovableEntityMutationSpec extends FlatSpec with Matchers {

  "RemovableEntityMutation" should "schedule entity removal" in {
    val initial = TestCreatureFactory.generate(State(), 2)
    val creature1 = CreatureId(1)
    val creature2 = CreatureId(2)

    val mutated = RemovableEntityMutation(creature1)(initial)
    mutated.tmp.removableEntities should ===(Set(creature1))

    val mutatedAgain = RemovableEntityMutation(creature2)(mutated)
    mutatedAgain.tmp.removableEntities should ===(Set(creature2, creature1))
  }

  it should "schedule removal for entity statuses" in {
    val initial = TestCreatureFactory.generate(State(), 2)
    val creature1 = CreatureId(1)
    val creature2 = CreatureId(2)
    val status1 = TestActiveStatus(ActiveStatusId(3), initial.tick, creature1)
    val status2 = TestActiveStatus(ActiveStatusId(4), initial.tick, creature2)

    val mutated = RemovableEntityMutation(creature1)(NewEntityMutation(status2)(NewEntityMutation(status1)(initial)))
    mutated.tmp.removableEntities should ===(Set(creature1, status1.id))
  }

  it should "schedule removal for entity items" in {
    val initial = TestCreatureFactory.generate(State())
    val creature1 = CreatureId(1)
    val item1 = Item(ItemId(2), ItemKindId("item"), creature1)
    val item2 = Item(ItemId(3), ItemKindId("item"), creature1)

    val mutated = RemovableEntityMutation(creature1)(NewEntityMutation(item2)(NewEntityMutation(item1)(initial)))
    mutated.tmp.removableEntities should ===(Set(creature1, item1.id, item2.id))
  }

  it should "allow duplicate removal but ignore it" in {
    val initial = TestCreatureFactory.generate(State())
    val mutated = RemovableEntityMutation(CreatureId(1))(initial)
    RemovableEntityMutation(CreatureId(1))(mutated) should ===(mutated)
  }

  it should "schedule removal for sub entities" in {
    var initial = State()
    initial = TestCreatureFactory.generate(initial)
    initial = TestItemFactory.generate(initial, 1, CreatureId(1))
    initial = TestItemFactory.generate(initial, 2, ItemId(2))
    initial = TestCreatureFactory.generate(initial)

    val mutated = RemovableEntityMutation(CreatureId(1))(initial)
    mutated.tmp.removableEntities should ===(Set(CreatureId(1), ItemId(2), ItemId(3), ItemId(4)))
  }
}
