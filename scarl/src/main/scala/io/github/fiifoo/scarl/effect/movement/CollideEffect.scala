package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{EntityId, LocatableId}

case class CollideEffect(target: LocatableId,
                         obstacle: EntityId,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
