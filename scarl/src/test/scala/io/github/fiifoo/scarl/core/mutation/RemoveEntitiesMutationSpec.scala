package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory, TestTriggerStatus}
import org.scalatest._

class RemoveEntitiesMutationSpec extends FlatSpec with Matchers {

  "RemoveEntitiesMutation" should "remove entities" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = Set(CreatureId(1), CreatureId(3)))),
      3
    )
    val creature1 = CreatureId(1)
    val creature2 = CreatureId(2)
    val creature3 = CreatureId(3)

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.entities.size should ===(1)
    mutated.entities.get(creature1).isEmpty should ===(true)
    mutated.entities.get(creature2).isEmpty should ===(false)
    mutated.entities.get(creature3).isEmpty should ===(true)
    mutated.tmp.removableEntities should ===(Set())
  }

  it should "remove creature equipments" in {
    val initial = TestCreatureFactory.generate(
      State(
        tmp = State.Temporary(removableEntities = Set(CreatureId(1), CreatureId(3))),
        creature = State.Creature(
          equipments = Map(
            CreatureId(1) -> Map(),
            CreatureId(2) -> Map(MainHand -> ItemId(10)),
            CreatureId(3) -> Map(OffHand -> ItemId(11))
          ))
      ),
      3
    )

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.creature.equipments should ===(Map(CreatureId(2) -> Map(MainHand -> ItemId(10))))
  }

  it should "remove creature tactics" in {
    val initial = TestCreatureFactory.generate(
      State(
        tmp = State.Temporary(removableEntities = Set(CreatureId(1), CreatureId(3))),
        creature = State.Creature(
          tactics = Map(CreatureId(1) -> RoamTactic, CreatureId(2) -> RoamTactic, CreatureId(3) -> RoamTactic)
        ),
      ),
      3
    )

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.creature.tactics should ===(Map(CreatureId(2) -> RoamTactic))
  }

  it should "mutate entity location index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = Set(CreatureId(1), CreatureId(3)))),
      3
    )

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.index.locationEntities should ===(Map(Location(0, 0) -> Set(CreatureId(2))))
  }

  it should "mutate status target index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = Set(ActiveStatusId(3))))
    )
    val status1 = TestActiveStatus(ActiveStatusId(2), initial.tick, CreatureId(1))
    val status2 = TestActiveStatus(ActiveStatusId(3), initial.tick, CreatureId(1))

    val mutated = RemoveEntitiesMutation()(NewEntityMutation(status2)(NewEntityMutation(status1)(initial)))
    mutated.index.targetStatuses should ===(Map(CreatureId(1) -> Set(ActiveStatusId(2))))
  }

  it should "mutate item container index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = Set(ItemId(3))))
    )
    val item1 = Item(ItemId(2), ItemKindId("item"), CreatureId(1))
    val item2 = Item(ItemId(3), ItemKindId("item"), CreatureId(1))

    val mutated = RemoveEntitiesMutation()(NewEntityMutation(item2)(NewEntityMutation(item1)(initial)))
    mutated.index.containerItems should ===(Map(CreatureId(1) -> Set(ItemId(2))))
  }

  it should "mutate faction member index" in {
    val faction = FactionId("them")
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = Set(CreatureId(1), CreatureId(3)))),
      3,
      TestCreatureFactory.create(faction = faction)
    )

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.index.factionMembers should ===(Map(faction -> Set(CreatureId(2))))
  }

  it should "mutate trigger location index" in {
    val location = Location(1, 1)
    val c1 = Container(ContainerId(1), location)
    val c2 = Container(ContainerId(2), location)
    val s1 = TestTriggerStatus(TriggerStatusId(3), c1.id)
    val s2 = TestTriggerStatus(TriggerStatusId(4), c2.id)
    val initial = NewEntityMutation(s2)(NewEntityMutation(s1)(NewEntityMutation(c2)(NewEntityMutation(c1)(State(
      tmp = State.Temporary(removableEntities = Set(s1.id))
    )))))

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.index.locationTriggers should ===(Map(location -> Set(s2.id)))
  }

  it should "throw exception if entity does not exist" in {
    intercept[Exception] {
      RemoveEntitiesMutation()(State(tmp = State.Temporary(removableEntities = Set(CreatureId(1)))))
    }
  }
}
