package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity.{CreatureId, UsableId}

case class NewConversationMutation(source: UsableId, target: CreatureId, communication: CommunicationId) extends Mutation {

  def apply(s: State): State = {
    s.copy(creature = s.creature.copy(
      conversations = s.creature.conversations + (this.target -> (this.source -> this.communication))
    ))
  }
}
