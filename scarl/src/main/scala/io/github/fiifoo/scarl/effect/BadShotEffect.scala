package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId}

case class BadShotEffect(attacker: CreatureId,
                         obstacle: Option[EntityId],
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
