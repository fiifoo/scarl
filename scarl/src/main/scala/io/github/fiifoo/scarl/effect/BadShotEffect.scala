package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, WallId}

case class BadShotEffect(attacker: CreatureId,
                         wall: Option[WallId],
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
