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
    EffectResult(
      CommunicationReceivedMutation(this.target(s).faction, this.communication)
    )
  }
}
