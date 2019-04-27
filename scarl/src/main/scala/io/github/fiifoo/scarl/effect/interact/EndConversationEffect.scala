package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.RemoveConversationMutation

case class EndConversationEffect(creature: CreatureId,
                                 parent: Option[Effect] = None
                                ) extends Effect {
  def apply(s: State): EffectResult = {
    EffectResult(
      RemoveConversationMutation(this.creature)
    )
  }
}
