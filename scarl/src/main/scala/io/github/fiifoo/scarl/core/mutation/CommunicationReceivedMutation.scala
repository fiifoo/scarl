package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.FactionId

case class CommunicationReceivedMutation(faction: FactionId, communication: CommunicationId) extends Mutation {

  def apply(s: State): State = {
    val received = s.creature.receivedCommunications.get(faction) map (received => {
      received + communication
    }) getOrElse {
      Set(communication)
    }

    s.copy(creature = s.creature.copy(
      receivedCommunications = s.creature.receivedCommunications + (faction -> received)
    ))
  }
}
