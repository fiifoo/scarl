package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId

case class RemoveConversationMutation(creature: CreatureId) extends Mutation {

  def apply(s: State): State = {
    s.copy(creature = s.creature.copy(
      conversations = s.creature.conversations - this.creature
    ))
  }
}
