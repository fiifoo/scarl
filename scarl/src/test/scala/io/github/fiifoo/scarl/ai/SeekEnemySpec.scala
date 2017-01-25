package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class SeekEnemySpec extends FlatSpec with Matchers {

  "SeekEnemy" should "find closest enemy" in {
    val c1 = TestCreatureFactory.create(CreatureId(1))
    val c2 = TestCreatureFactory.create(CreatureId(2), location = Location(5, 0))
    val c3 = TestCreatureFactory.create(CreatureId(3), location = Location(2, 0))

    val s = NewEntityMutation(c3)(NewEntityMutation(c2)(NewEntityMutation(c1)(State())))

    SeekEnemy(s, CreatureId(1)(s)) should ===(Some(c3))
  }
}
