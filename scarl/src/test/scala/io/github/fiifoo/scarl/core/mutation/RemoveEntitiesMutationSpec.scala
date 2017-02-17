package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory}
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class RemoveEntitiesMutationSpec extends FlatSpec with Matchers {

  "RemoveEntitiesMutation" should "remove entities" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = List(CreatureId(1), CreatureId(3)))),
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
    mutated.tmp.removableEntities should ===(List())
  }

  it should "mutate entity location index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = List(CreatureId(1), CreatureId(3)))),
      3
    )

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.index.locationEntities should ===(Map(Location(0, 0) -> List(CreatureId(2))))
  }

  it should "mutate status target index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = List(ActiveStatusId(3))))
    )
    val status1 = TestActiveStatus(ActiveStatusId(2), initial.tick, CreatureId(1))
    val status2 = TestActiveStatus(ActiveStatusId(3), initial.tick, CreatureId(1))

    val mutated = RemoveEntitiesMutation()(NewEntityMutation(status2)(NewEntityMutation(status1)(initial)))
    mutated.index.targetStatuses should ===(Map(CreatureId(1) -> List(ActiveStatusId(2))))
  }

  it should "mutate item container index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = State.Temporary(removableEntities = List(ItemId(3))))
    )
    val item1 = Item(ItemId(2), ItemKindId("item"), CreatureId(1))
    val item2 = Item(ItemId(3), ItemKindId("item"), CreatureId(1))

    val mutated = RemoveEntitiesMutation()(NewEntityMutation(item2)(NewEntityMutation(item1)(initial)))
    mutated.index.containerItems should ===(Map(CreatureId(1) -> List(ItemId(2))))
  }

  it should "throw exception if entity does not exist" in {
    intercept[Exception] {
      RemoveEntitiesMutation()(State(tmp = State.Temporary(removableEntities = List(CreatureId(1)))))
    }
  }
}
