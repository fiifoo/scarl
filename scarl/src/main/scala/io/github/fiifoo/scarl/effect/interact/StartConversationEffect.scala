package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, UsableId}
import io.github.fiifoo.scarl.core.mutation.NewConversationMutation

case class StartConversationEffect(source: UsableId,
                                   target: CreatureId,
                                   communication: CommunicationId,
                                   parent: Option[Effect] = None
                                  ) extends Effect {
  def apply(s: State): EffectResult = {
    EffectResult(
      NewConversationMutation(this.source, this.target, this.communication)
    )
  }
}
