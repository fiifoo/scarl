package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.CommunicationReceivedMutation

case class CommunicateEffect(source: CreatureId,
                             target: CreatureId,
                             communication: Option[CommunicationId],
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    val receivedMutation = communication map (CommunicationReceivedMutation(target(s).faction, _))
    val responseEffect = if (parent.forall(!_.isInstanceOf[CommunicateEffect])) {
      val response = nextResponse(s)
      val effect = CommunicateEffect(target, source, response, Some(this))

      Some(effect)
    } else {
      None
    }

    EffectResult(
      List(receivedMutation).flatten,
      List(responseEffect).flatten
    )
  }

  private def nextResponse(s: State): Option[CommunicationId] = {
    val faction = source(s).faction
    val responses = s.assets.kinds.creatures(target(s).kind).responses.get(faction)

    responses map (responses => {
      val received = s.creature.receivedCommunications.getOrElse(faction, Set())
      val nextResponses = responses filterNot received.contains

      if (nextResponses.isEmpty) {
        responses.last
      } else {
        nextResponses.head
      }
    })
  }
}
