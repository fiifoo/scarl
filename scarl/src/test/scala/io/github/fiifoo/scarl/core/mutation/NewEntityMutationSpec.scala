package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId, Item, ItemId}
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory}
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class NewEntityMutationSpec extends FlatSpec with Matchers {

  "NewEntityMutation" should "mutate new entity" in {
    val existingCreature = TestCreatureFactory.create(CreatureId(1))
    val newCreature = TestCreatureFactory.create(CreatureId(2))
    val initial = State(entities = Map(existingCreature.id -> existingCreature), nextEntityId = 2)

    val mutated = NewEntityMutation(newCreature)(initial)
    mutated.entities.size should ===(2)
    existingCreature.id(mutated) should ===(existingCreature)
    newCreature.id(mutated) should ===(newCreature)
  }

  it should "mutate entity location index" in {
    val location = Location(1, 1)
    val creature1 = TestCreatureFactory.create(CreatureId(1), location = location)
    val creature2 = TestCreatureFactory.create(CreatureId(2), location = location)
    val initial = State()

    val mutated = NewEntityMutation(creature1)(initial)
    mutated.index.locationEntities should ===(Map(location -> List(CreatureId(1))))

    val mutatedAgain = NewEntityMutation(creature2)(mutated)
    mutatedAgain.index.locationEntities should ===(Map(location -> List(CreatureId(2), CreatureId(1))))
  }

  it should "mutate status target index" in {
    val initial = TestCreatureFactory.generate(State())
    val status1 = TestActiveStatus(ActiveStatusId(2), initial.tick, CreatureId(1))
    val status2 = TestActiveStatus(ActiveStatusId(3), initial.tick, CreatureId(1))

    val mutated = NewEntityMutation(status1)(initial)
    mutated.index.targetStatuses should ===(Map(CreatureId(1) -> List(ActiveStatusId(2))))

    val mutatedAgain = NewEntityMutation(status2)(mutated)
    mutatedAgain.index.targetStatuses should ===(Map(CreatureId(1) -> List(ActiveStatusId(3), ActiveStatusId(2))))
  }

  it should "mutate item container index" in {
    val initial = TestCreatureFactory.generate(State())
    val item1 = Item(ItemId(2), CreatureId(1))
    val item2 = Item(ItemId(3), CreatureId(1))

    val mutated = NewEntityMutation(item1)(initial)
    mutated.index.containerItems should ===(Map(CreatureId(1) -> List(ItemId(2))))

    val mutatedAgain = NewEntityMutation(item2)(mutated)
    mutatedAgain.index.containerItems should ===(Map(CreatureId(1) -> List(ItemId(3), ItemId(2))))
  }
}
