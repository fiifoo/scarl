package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.EffectResult
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{CreatureDeadMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.effect.combat.DeathEffect
import org.scalatest._

class DeathEffectSpec extends FlatSpec with Matchers {

  "DeathEffect" should "return creature dead and removable entity mutations" in {
    val s = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)
    val location = creature(s).location

    DeathEffect(creature, location)(s) should ===(EffectResult(List(
      CreatureDeadMutation(CreatureId(1)),
      RemovableEntityMutation(CreatureId(1))
    )))
  }

  it should "return empty result if creature is already dead" in {
    val s = TestCreatureFactory.generate(State(), prototype = TestCreatureFactory.create(dead = true))
    val creature = CreatureId(1)
    val creatureLocation = creature(s).location

    DeathEffect(creature, creatureLocation)(s) should ===(EffectResult())
  }
}
