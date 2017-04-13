package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.{Location, State}

case class CollideEffect(target: LocatableId,
                         location: Location,
                         obstacle: LocatableId,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
