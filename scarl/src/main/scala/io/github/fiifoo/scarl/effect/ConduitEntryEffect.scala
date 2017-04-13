package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{ConduitEntryMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.core.{ConduitId, State}

case class ConduitEntryEffect(creature: CreatureId,
                              conduit: ConduitId,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(List(
      ConduitEntryMutation(creature, conduit),
      RemovableEntityMutation(creature)
    ))
  }
}
