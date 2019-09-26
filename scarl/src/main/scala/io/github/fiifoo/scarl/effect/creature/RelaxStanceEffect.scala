package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{StanceStatus, StatusId}
import io.github.fiifoo.scarl.core.mutation.StanceDurationMutation

case class RelaxStanceEffect(target: StatusId,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutation = this.target(s) match {
      case status: StanceStatus => status.duration map (duration => {
        if (status.continuous && duration <= 1) {
          StanceDurationMutation(status, None)
        } else
          StanceDurationMutation(status, Some(duration - 1))
      })
      case _ => None
    }

    mutation map (EffectResult(_)) getOrElse EffectResult()
  }
}
