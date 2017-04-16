package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CommunicationReceivedMutation(creature: CreatureId, communication: CommunicationId) extends Mutation {

  def apply(s: State): State = {
    val received = s.communications.received.get(creature) map (received => {
      received + communication
    }) getOrElse {
      Set(communication)
    }

    s.copy(communications = s.communications.copy(
      received = s.communications.received + (creature -> received)
    ))
  }
}
