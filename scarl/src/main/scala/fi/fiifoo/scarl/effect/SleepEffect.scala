package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId}
import fi.fiifoo.scarl.core.mutation.NewEntityMutation
import fi.fiifoo.scarl.status.SleepStatus

case class SleepEffect(target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    val status = SleepStatus(ActiveStatusId(s.nextEntityId), s.tick, target)

    EffectResult(NewEntityMutation(status))
  }
}
