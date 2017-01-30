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

    getLocationEntities(s)(location) should ===(List(
      CreatureId(3),
      CreatureId(2)
    ))
  }

  it should "give target statuses" in {
    val s =
      NewEntityMutation(TestActiveStatus(ActiveStatusId(5), 1, CreatureId(2)))(
        NewEntityMutation(TestActiveStatus(ActiveStatusId(4), 1, CreatureId(1)))(
          NewEntityMutation(TestActiveStatus(ActiveStatusId(3), 1, CreatureId(1)))(
            TestCreatureFactory.generate(State(), 2)
          )))

    getTargetStatuses(s)(CreatureId(1)) should ===(List(
      ActiveStatusId(4),
      ActiveStatusId(3)
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

    getContainerItems(s)(CreatureId(1)) should ===(List(
      ItemId(4),
      ItemId(3)
    ))
  }
}
