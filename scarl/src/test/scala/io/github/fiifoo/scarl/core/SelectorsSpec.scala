package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.Selectors._
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory, TestItemFactory}
import org.scalatest._

class SelectorsSpec extends FlatSpec with Matchers {

  "Selectors" should "give location entities" in {
    val location = Location(5, 5)
    val s =
      TestCreatureFactory.generate(
        TestCreatureFactory.generate(
          State()
        ),
        2,
        TestCreatureFactory.create(location = location)
      )

    getLocationEntities(location)(s) should ===(List(
      CreatureId(3)(s),
      CreatureId(2)(s)
    ))
  }

  it should "give target statuses" in {
    val s =
      NewEntityMutation(TestActiveStatus(ActiveStatusId(5), 1, CreatureId(2)))(
        NewEntityMutation(TestActiveStatus(ActiveStatusId(4), 1, CreatureId(1)))(
          NewEntityMutation(TestActiveStatus(ActiveStatusId(3), 1, CreatureId(1)))(
            TestCreatureFactory.generate(State(), 2)
          )))

    getTargetStatuses(CreatureId(1))(s) should ===(List(
      ActiveStatusId(4)(s),
      ActiveStatusId(3)(s)
    ))
  }

  it should "give container items" in {
    val s =
      TestItemFactory.generate(
        TestItemFactory.generate(
          TestItemFactory.generate(
            State(),
            2,
            CreatureId(0)
          ),
          2,
          CreatureId(1)
        ),
        1,
        CreatureId(2)
      )

    getContainerItems(CreatureId(1))(s) should ===(List(
      ItemId(4)(s),
      ItemId(3)(s)
    ))
  }
}
