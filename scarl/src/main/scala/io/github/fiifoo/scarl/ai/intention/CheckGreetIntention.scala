package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.action.GreetAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object CheckGreetIntention extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    val greetings = s.assets.kinds.creatures(actor(s).kind).greetings

    if (greetings.isEmpty) {
      return None
    }

    val factions = greetings.keys.toSet

    val greet = Utils.findTargets(s, actor, factions, 1).headOption flatMap (target => {
      val faction = target.faction
      val greeting = getNextGreeting(s, target.id, greetings(faction))

      greeting map (GreetAction(target.id, _))
    })

    greet map ((Utils.getTactic(s, actor), _))
  }

  private def getNextGreeting(s: State, target: CreatureId, greetings: List[CommunicationId]): Option[CommunicationId] = {
    val received = s.creature.receivedCommunications.getOrElse(target(s).faction, Set())

    greetings find (!received.contains(_))
  }
}
