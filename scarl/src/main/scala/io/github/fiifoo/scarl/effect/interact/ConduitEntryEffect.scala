package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{ConduitEntryMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.core.world.ConduitId

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
