package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.action.CommunicateAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity.CreatureId

object Greeting {

  def apply(s: State, actor: CreatureId): Option[CommunicateAction] = {
    val greetings = s.assets.kinds.creatures(actor(s).kind).greetings

    if (greetings.isEmpty) {
      return None
    }

    val factions = greetings.keys.toSet

    SeekTargets(s, actor, factions, 1).headOption flatMap (target => {
      val faction = target.faction
      val greeting = nextGreeting(s, target.id, greetings(faction))

      greeting map (_ => CommunicateAction(target.id, greeting))
    })
  }

  private def nextGreeting(s: State, target: CreatureId, greetings: List[CommunicationId]): Option[CommunicationId] = {
    val received = s.receivedCommunications.getOrElse(target, Set())

    (greetings filterNot received.contains).headOption
  }
}
