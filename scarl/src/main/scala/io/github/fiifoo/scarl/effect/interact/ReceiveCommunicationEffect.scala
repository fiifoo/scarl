package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, UsableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.CommunicationReceivedMutation

case class ReceiveCommunicationEffect(source: UsableId,
                                      target: CreatureId,
                                      communication: CommunicationId,
                                      location: Location,
                                      parent: Option[Effect] = None
                                     ) extends Effect {

  def apply(s: State): EffectResult = {
    val communication = this.communication(s)
    val faction = this.target(s).faction

    val conversationEffect = if (communication.validChoices(this.target)(s).isEmpty) {
      if (s.creature.conversations.isDefinedAt(this.target)) {
        Some(EndConversationEffect(this.target))
      } else {
        None
      }
    } else {
      Some(StartConversationEffect(this.source, this.target, this.communication))
    }

    val received = s.creature.receivedCommunications.getOrElse(faction, Set())
    val power = if (received.contains(this.communication)) None else this.source match {
      case _: CreatureId => communication.creaturePower
      case _: ItemId => communication.itemPower
    }
    val powerEffect = power map (PowerUseEffect(Some(this.target), this.source, _, requireResources = false))

    EffectResult(
      CommunicationReceivedMutation(faction, this.communication),
      List(
        conversationEffect,
        powerEffect
      ).flatten
    )
  }
}
