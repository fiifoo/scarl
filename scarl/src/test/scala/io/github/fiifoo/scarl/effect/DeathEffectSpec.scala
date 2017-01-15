package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.EffectResult
import fi.fiifoo.scarl.core.entity.{CreatureId, PassiveStatusId}
import fi.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}
import fi.fiifoo.scarl.core.test_assets.TestCreatureFactory
import fi.fiifoo.scarl.status.DeathStatus
import org.scalatest._

class DeathEffectSpec extends FlatSpec with Matchers {

  "DeathEffect" should "return death status and removable entity mutations" in {
    val s = TestCreatureFactory.generate(State())

    DeathEffect(CreatureId(1))(s) should ===(EffectResult(List(
      NewEntityMutation(DeathStatus(PassiveStatusId(2), CreatureId(1))),
      RemovableEntityMutation(CreatureId(1))
    )))
  }

  it should "return empty result if creature already has death status" in {
    val s = NewEntityMutation(DeathStatus(PassiveStatusId(2), CreatureId(1)))(TestCreatureFactory.generate(State()))

    DeathEffect(CreatureId(1))(s) should ===(EffectResult())
  }
}
