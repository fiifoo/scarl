package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class CommunicateEffect(source: CreatureId,
                             target: CreatureId,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    val effect = this.getNextResponse(s) map (response => {
      ReceiveCommunicationEffect(this.target, this.source, response, this.source(s).location, Some(this))
    }) getOrElse {
      NoResponseEffect(this.target, this.source, Some(this))
    }

    EffectResult(effect)
  }

  private def getNextResponse(s: State): Option[CommunicationId] = {
    val faction = this.source(s).faction
    val responses = s.assets.kinds.creatures(this.target(s).kind).responses.get(faction)

    responses flatMap (responses => {
      val received = s.creature.receivedCommunications.getOrElse(faction, Set())

      responses find (!received.contains(_)) orElse {
        responses find (_.apply(s).repeatable)
      }
    })
  }
}
