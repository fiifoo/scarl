package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class MissEffect(attacker: CreatureId, target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
