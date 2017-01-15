package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.CreatureId
import fi.fiifoo.scarl.core.{Location, State}

case class TestKaboomEffect(target: CreatureId) extends Effect {
  val damage = 9001
  val location = Location(9999, 9999)

  def apply(s: State): EffectResult = {
    val damageEffect = TestDamageEffect(target, damage)
    val moveEffect = TestMoveEffect(target, location)

    EffectResult(List(damageEffect, moveEffect))
  }
}
