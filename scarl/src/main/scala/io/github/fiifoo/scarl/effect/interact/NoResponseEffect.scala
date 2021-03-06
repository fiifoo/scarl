package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class NoResponseEffect(source: CreatureId,
                            target: CreatureId,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
