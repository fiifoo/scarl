package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.EffectResult
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.CreatureDamageMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class DamageEffectSpec extends FlatSpec with Matchers {

  "DamageEffect" should "return damage mutation" in {
    val health = 10
    val damage = 1

    val s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(health = health))

    DamageEffect(CreatureId(1), damage)(s) should ===(EffectResult(
      CreatureDamageMutation(CreatureId(1), damage)))
  }

  it should "return death effect if damage exceeds creature health" in {
    val health = 5
    val damage = 5

    val s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(health = health))
    val effect = DamageEffect(CreatureId(1), damage)

    effect(s) should ===(EffectResult(
      CreatureDamageMutation(CreatureId(1), damage),
      DeathEffect(CreatureId(1), Some(effect))
    ))
  }

  it should "not return death effect if health has already been exceeded" in {
    val health = 10
    val initialDamage = 10
    val damage = 1

    val s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(health = health, damage = initialDamage))

    DamageEffect(CreatureId(1), damage)(s) should ===(EffectResult(
      CreatureDamageMutation(CreatureId(1), initialDamage + damage)
    ))
  }
}
