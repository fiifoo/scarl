package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.EffectResult
import io.github.fiifoo.scarl.core.entity.{Container, ContainerId, CreatureId, TriggerStatusId}
import io.github.fiifoo.scarl.core.mutation.{LocatableLocationMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.test_assets.{TestCreatureFactory, TestDamageEffect, TestTriggerStatus}
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class MoveEffectSpec extends FlatSpec with Matchers {

  "MoveEffect" should "return location mutation" in {
    val s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create())
    val location = Location(1, 0)

    MoveEffect(CreatureId(1), location)(s) should ===(EffectResult(
      LocatableLocationMutation(CreatureId(1), location)))
  }

  it should "return trigger effects from triggers in target location" in {
    val location = Location(1, 0)
    val c1 = Container(ContainerId(1), location)
    val c2 = Container(ContainerId(2), Location(0, 1))
    val c3 = Container(ContainerId(3), location)
    val s1 = TestTriggerStatus(TriggerStatusId(4), c1.id)
    val s2 = TestTriggerStatus(TriggerStatusId(5), c2.id)
    val s3 = TestTriggerStatus(TriggerStatusId(6), c3.id)

    val New = NewEntityMutation
    val s = TestCreatureFactory.generate(
      New(s3)(New(s2)(New(s1)(New(c3)(New(c2)(New(c1)(State()))))))
    )
    val creature = CreatureId(7)
    val damageEffect = TestDamageEffect(creature, s1.damage)

    MoveEffect(creature, location)(s) should ===(EffectResult(
      LocatableLocationMutation(creature, location),
      List(damageEffect, damageEffect)
    ))
  }
}
