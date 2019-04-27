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
    val mutation = CommunicationReceivedMutation(this.target(s).faction, this.communication)

    val effects = (this.source match {
      case creature: CreatureId => this.communication(s).creaturePower map (_.apply(s, creature, Some(this.target)))
      case item: ItemId => this.communication(s).itemPower map (_.apply(s, item, Some(this.target)))
    }) getOrElse {
      List()
    }

    EffectResult(
      mutation,
      effects
    )
  }
}
