package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import org.scalatest._

class ResetAddedActorsMutationSpec extends FlatSpec with Matchers {

  "ResetAddedActorsMutation" should "mutate correctly" in {
    val initial = State(tmp = State.Temporary(addedActors = List(CreatureId(1))))

    val mutated = ResetAddedActorsMutation()(initial)
    mutated.tmp.addedActors should ===(List())
  }

}
