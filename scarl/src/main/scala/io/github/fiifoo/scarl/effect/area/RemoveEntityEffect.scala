package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.EntityId
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class RemoveEntityEffect(target: EntityId,
                              location: Option[Location] = None,
                              description: Option[String] = None,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(RemovableEntityMutation(target))
  }
}
