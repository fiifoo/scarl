package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.{State, TmpState}
import org.scalatest._

class ResetAddedActorsMutationSpec extends FlatSpec with Matchers {

  "ResetAddedActorsMutation" should "mutate correctly" in {
    val initial = State(tmp = TmpState(addedActors = List(CreatureId(1))))

    val mutated = ResetAddedActorsMutation()(initial)
    mutated.tmp.addedActors should ===(List())
  }

}
