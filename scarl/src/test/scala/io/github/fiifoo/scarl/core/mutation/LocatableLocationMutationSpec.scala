package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class LocatableLocationMutationSpec extends FlatSpec with Matchers {

  "EntityLocationMutation" should "mutate entity" in {
    val location = Location(5, 5)
    val initial = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val mutated = LocatableLocationMutation(creature, location)(initial)
    creature(mutated).location should ===(location)
  }

  it should "mutate index" in {
    val location1 = Location(5, 5)
    val location2 = Location(10, 10)
    val initial = TestCreatureFactory.generate(State(), 4)
    val creature1 = CreatureId(1)
    val creature2 = CreatureId(2)
    val creature3 = CreatureId(3)
    val creature4 = CreatureId(4)

    val mutation1 = LocatableLocationMutation(creature1, location1)
    val mutation2 = LocatableLocationMutation(creature2, location2)
    val mutation3 = LocatableLocationMutation(creature3, location2)

    val mutated = mutation3(mutation2(mutation1(initial)))
    val should = Map(
      Location(0, 0) -> Set(creature4),
      location1 -> Set(creature1),
      location2 -> Set(creature2, creature3)
    )

    mutated.index.locationEntities should ===(should)
  }

}
