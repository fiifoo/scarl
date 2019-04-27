package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.interact.ReceiveCommunicationEffect

case class ReceiveCommunicationPower(description: Option[String] = None,
                                     resources: Option[Resources] = None,
                                     communication: CommunicationId,
                                    ) extends ItemPower {
  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    user map (user => {
      List(
        ReceiveCommunicationEffect(item, user, communication, user(s).location)
      )
    }) getOrElse {
      List()
    }
  }
}
