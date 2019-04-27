package io.github.fiifoo.scarl.core.communication

import io.github.fiifoo.scarl.core.entity.{CreatureId, CreaturePower, ItemPower}
import io.github.fiifoo.scarl.core.{State, Text}

object Communication {

  case class Choice(communication: CommunicationId, description: String)

  def select(communications: List[CommunicationId], creature: CreatureId)(s: State): Option[CommunicationId] = {
    val faction = creature(s).faction
    val received = s.creature.receivedCommunications.getOrElse(faction, Set())

    def extractInterrupted(communication: CommunicationId): Option[CommunicationId] = {
      val choices = communication(s).choices map (_.communication)

      if (choices.nonEmpty) {
        if (!choices.exists(received.contains)) {
          Some(communication)
        } else {
          (choices flatMap extractInterrupted).headOption
        }
      } else {
        None
      }
    }

    def extract(communication: CommunicationId): Option[CommunicationId] = {
      if (!received.contains(communication)) {
        Some(communication)
      } else {
        extractInterrupted(communication)
      }
    }

    (communications flatMap extract).headOption orElse {
      communications find (_.apply(s).repeatable)
    }
  }
}

case class Communication(id: CommunicationId,
                         message: Text,
                         repeatable: Boolean = false,
                         creaturePower: Option[CreaturePower] = None,
                         itemPower: Option[ItemPower] = None,
                         choices: List[Communication.Choice] = List(),
                        ) {
  def validChoices(creature: CreatureId)(s: State): List[Communication.Choice] = {
    val faction = creature(s).faction
    val received = s.creature.receivedCommunications.getOrElse(faction, Set())

    this.choices filter (x => x.communication(s).repeatable || !received.contains(x.communication))
  }
}
