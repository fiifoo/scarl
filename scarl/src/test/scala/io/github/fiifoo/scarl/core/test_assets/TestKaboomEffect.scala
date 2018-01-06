package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

case class TestKaboomEffect(target: CreatureId,
                            parent: Option[Effect] = None
                           ) extends Effect {
  val damage = 9001
  val location = Location(9999, 9999)

  def apply(s: State): EffectResult = {
    val damageEffect = TestDamageEffect(target, damage)
    val moveEffect = TestMoveEffect(target, location)

    EffectResult(List(damageEffect, moveEffect))
  }
}
