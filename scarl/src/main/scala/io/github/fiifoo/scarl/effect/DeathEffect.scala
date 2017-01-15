package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.Selectors.getTargetStatuses
import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.{CreatureId, PassiveStatusId}
import fi.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}
import fi.fiifoo.scarl.status.DeathStatus

case class DeathEffect(target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    if (getTargetStatuses(target)(s) exists (_.isInstanceOf[DeathStatus])) {
      return EffectResult()
    }

    val status = DeathStatus(PassiveStatusId(s.nextEntityId), target)

    EffectResult(List(
      NewEntityMutation(status),
      RemovableEntityMutation(target)
    ))
  }
}
