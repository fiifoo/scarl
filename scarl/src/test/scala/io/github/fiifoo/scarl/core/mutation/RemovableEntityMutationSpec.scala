package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId, Item, ItemId}
import fi.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory}
import org.scalatest._

class RemovableEntityMutationSpec extends FlatSpec with Matchers {

  "RemovableEntityMutation" should "schedule entity removal" in {
    val initial = TestCreatureFactory.generate(State(), 2)
    val creature1 = CreatureId(1)
    val creature2 = CreatureId(2)

    val mutated = RemovableEntityMutation(creature1)(initial)
    mutated.tmp.removableEntities should ===(List(creature1))

    val mutatedAgain = RemovableEntityMutation(creature2)(mutated)
    mutatedAgain.tmp.removableEntities should ===(List(creature2, creature1))
  }

  it should "schedule removal for entity statuses" in {
    val initial = TestCreatureFactory.generate(State(), 2)
    val creature1 = CreatureId(1)
    val creature2 = CreatureId(2)
    val status1 = TestActiveStatus(ActiveStatusId(3), initial.tick, creature1)
    val status2 = TestActiveStatus(ActiveStatusId(4), initial.tick, creature2)

    val mutated = RemovableEntityMutation(creature1)(NewEntityMutation(status2)(NewEntityMutation(status1)(initial)))
    mutated.tmp.removableEntities should ===(List(creature1, status1.id))
  }

  it should "schedule removal for entity items" in {
    val initial = TestCreatureFactory.generate(State())
    val creature1 = CreatureId(1)
    val item1 = Item(ItemId(2), creature1)
    val item2 = Item(ItemId(3), creature1)

    val mutated = RemovableEntityMutation(creature1)(NewEntityMutation(item2)(NewEntityMutation(item1)(initial)))
    mutated.tmp.removableEntities should ===(List(creature1, item2.id, item1.id))
  }

  it should "allow duplicate removal but ignore it" in {
    val initial = TestCreatureFactory.generate(State())
    val mutated = RemovableEntityMutation(CreatureId(1))(initial)
    RemovableEntityMutation(CreatureId(1))(mutated) should ===(mutated)
  }

}
