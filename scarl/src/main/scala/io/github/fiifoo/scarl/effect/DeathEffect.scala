package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getTargetStatuses
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, PassiveStatusId}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.status.DeathStatus

case class DeathEffect(target: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    if (getTargetStatuses(s)(target) exists (_ (s).isInstanceOf[DeathStatus])) {
      return EffectResult()
    }

    val status = DeathStatus(PassiveStatusId(s.nextEntityId), target)

    EffectResult(List(
      NewEntityMutation(status),
      RemovableEntityMutation(target)
    ))
  }
}
