package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class CreatureDamageMutationSpec extends FlatSpec with Matchers {

  "CreatureDamageMutation" should "mutate correctly" in {
    val damage = 5
    val initial = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val mutated = CreatureDamageMutation(creature, damage)(initial)
    creature(mutated).damage should ===(damage)
  }

}
