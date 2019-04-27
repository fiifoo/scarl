package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.interact.ReceiveCommunicationEffect

case class GreetAction(target: CreatureId, greeting: CommunicationId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor),
      ReceiveCommunicationEffect(actor, this.target, greeting, actor(s).location)
    )
  }
}
