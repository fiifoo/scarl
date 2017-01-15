package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.entity._
import fi.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory}
import fi.fiifoo.scarl.core.{Location, State, TmpState}
import org.scalatest._

class RemoveEntitiesMutationSpec extends FlatSpec with Matchers {

  "RemoveEntitiesMutation" should "remove entities" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = TmpState(removableEntities = List(CreatureId(1), CreatureId(3)))),
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
      State(tmp = TmpState(removableEntities = List(CreatureId(1), CreatureId(3)))),
      3
    )

    val mutated = RemoveEntitiesMutation()(initial)
    mutated.index.entities.location should ===(Map(Location(0, 0) -> List(CreatureId(2))))
  }

  it should "mutate status target index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = TmpState(removableEntities = List(ActiveStatusId(3)))),
      1
    )
    val status1 = TestActiveStatus(ActiveStatusId(2), initial.tick, CreatureId(1))
    val status2 = TestActiveStatus(ActiveStatusId(3), initial.tick, CreatureId(1))

    val mutated = RemoveEntitiesMutation()(NewEntityMutation(status2)(NewEntityMutation(status1)(initial)))
    mutated.index.statuses.target should ===(Map(CreatureId(1) -> List(ActiveStatusId(2))))
  }

  it should "mutate item container index" in {
    val initial = TestCreatureFactory.generate(
      State(tmp = TmpState(removableEntities = List(ItemId(3)))),
      1
    )
    val item1 = Item(ItemId(2), CreatureId(1))
    val item2 = Item(ItemId(3), CreatureId(1))

    val mutated = RemoveEntitiesMutation()(NewEntityMutation(item2)(NewEntityMutation(item1)(initial)))
    mutated.index.items.container should ===(Map(CreatureId(1) -> List(ItemId(2))))
  }

  it should "throw exception if entity does not exist" in {
    intercept[Exception] {
      RemoveEntitiesMutation()(State(tmp = TmpState(removableEntities = List(CreatureId(1)))))
    }
  }
}
