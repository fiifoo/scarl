package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.EntityId
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class RemoveEntityEffect(target: EntityId,
                              description: Option[String] = None,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(RemovableEntityMutation(target))
  }
}
