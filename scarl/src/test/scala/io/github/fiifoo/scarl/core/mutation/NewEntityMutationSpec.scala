package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory, TestItemFactory, TestTriggerStatus}
import org.scalatest._

class NewEntityMutationSpec extends FlatSpec with Matchers {

  "NewEntityMutation" should "mutate new entity" in {
    val existingCreature = TestCreatureFactory.create(CreatureId(1))
    val newCreature = TestCreatureFactory.create(CreatureId(2))
    val initial = State(entities = Map(existingCreature.id -> existingCreature), idSeq = IdSeq(2))

    val mutated = NewEntityMutation(newCreature)(initial)
    mutated.entities.size should ===(2)
    existingCreature.id(mutated) should ===(existingCreature)
    newCreature.id(mutated) should ===(newCreature)
  }

  it should "mutate actor queue" in {
    val location = Location(1, 1)
    val creature1 = TestCreatureFactory.create(CreatureId(1), location = location)
    val creature2 = TestCreatureFactory.create(CreatureId(2), location = location)
    val item = TestItemFactory.create(ItemId(3), container = CreatureId(1))
    val initial = State()

    val mutated = NewEntityMutation(creature1)(initial)
    mutated.cache.actorQueue should ===(ActorQueue(mutated))

    val mutatedAgain = NewEntityMutation(creature2)(mutated)
    mutatedAgain.cache.actorQueue should ===(ActorQueue(mutatedAgain))

    val mutatedMore = NewEntityMutation(item)(mutatedAgain)
    mutatedMore.cache.actorQueue should ===(ActorQueue(mutatedMore))
  }

  it should "mutate entity location index" in {
    val location = Location(1, 1)
    val creature1 = TestCreatureFactory.create(CreatureId(1), location = location)
    val creature2 = TestCreatureFactory.create(CreatureId(2), location = location)
    val initial = State()

    val mutated = NewEntityMutation(creature1)(initial)
    mutated.index.locationEntities should ===(Map(location -> Set(CreatureId(1))))

    val mutatedAgain = NewEntityMutation(creature2)(mutated)
    mutatedAgain.index.locationEntities should ===(Map(location -> Set(CreatureId(2), CreatureId(1))))
  }

  it should "mutate status target index" in {
    val initial = TestCreatureFactory.generate(State())
    val status1 = TestActiveStatus(ActiveStatusId(2), initial.tick, CreatureId(1))
    val status2 = TestActiveStatus(ActiveStatusId(3), initial.tick, CreatureId(1))

    val mutated = NewEntityMutation(status1)(initial)
    mutated.index.targetStatuses should ===(Map(CreatureId(1) -> Set(ActiveStatusId(2))))

    val mutatedAgain = NewEntityMutation(status2)(mutated)
    mutatedAgain.index.targetStatuses should ===(Map(CreatureId(1) -> Set(ActiveStatusId(3), ActiveStatusId(2))))
  }

  it should "mutate item container index" in {
    val initial = TestCreatureFactory.generate(State())
    val item1 = Item(ItemId(2), ItemKindId("item"), CreatureId(1))
    val item2 = Item(ItemId(3), ItemKindId("item"), CreatureId(1))

    val mutated = NewEntityMutation(item1)(initial)
    mutated.index.containerItems should ===(Map(CreatureId(1) -> Set(ItemId(2))))

    val mutatedAgain = NewEntityMutation(item2)(mutated)
    mutatedAgain.index.containerItems should ===(Map(CreatureId(1) -> Set(ItemId(3), ItemId(2))))
  }

  it should "mutate faction member index" in {
    val creature1 = TestCreatureFactory.create(CreatureId(1), faction = FactionId("1"))
    val creature2 = TestCreatureFactory.create(CreatureId(2), faction = FactionId("2"))
    val creature3 = TestCreatureFactory.create(CreatureId(3), faction = FactionId("1"))
    val initial = State()

    val mutated = NewEntityMutation(creature1)(initial)
    mutated.index.factionMembers should ===(Map(FactionId("1") -> Set(CreatureId(1))))

    val mutatedAgain = NewEntityMutation(creature2)(mutated)
    mutatedAgain.index.factionMembers should ===(Map(
      FactionId("1") -> Set(CreatureId(1)),
      FactionId("2") -> Set(CreatureId(2))
    ))

    val mutatedMore = NewEntityMutation(creature3)(mutatedAgain)
    mutatedMore.index.factionMembers should ===(Map(
      FactionId("1") -> Set(CreatureId(3), CreatureId(1)),
      FactionId("2") -> Set(CreatureId(2))
    ))
  }

  it should "mutate trigger location index" in {
    val location = Location(1, 1)
    val container1 = Container(ContainerId(1), location)
    val container2 = Container(ContainerId(2), location)
    val initial = NewEntityMutation(container2)(NewEntityMutation(container1)(State()))
    val status1 = TestTriggerStatus(TriggerStatusId(3), ContainerId(1))
    val status2 = TestTriggerStatus(TriggerStatusId(4), ContainerId(2))

    val mutated = NewEntityMutation(status1)(initial)
    mutated.index.locationTriggers should ===(Map(location -> Set(TriggerStatusId(3))))

    val mutatedAgain = NewEntityMutation(status2)(mutated)
    mutatedAgain.index.locationTriggers should ===(Map(location -> Set(TriggerStatusId(4), TriggerStatusId(3))))
  }
}
