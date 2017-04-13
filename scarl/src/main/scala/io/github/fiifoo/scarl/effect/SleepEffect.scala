package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.status.SleepStatus

case class SleepEffect(target: CreatureId,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    val status = SleepStatus(ActiveStatusId(s.nextEntityId), s.tick, target)

    EffectResult(NewEntityMutation(status))
  }
}
