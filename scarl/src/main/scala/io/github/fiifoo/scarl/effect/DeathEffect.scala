package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getTargetStatuses
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, PassiveStatusId}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.rule.GainExperienceRule
import io.github.fiifoo.scarl.status.DeathStatus

case class DeathEffect(target: CreatureId,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    if (getTargetStatuses(s)(target) exists (_ (s).isInstanceOf[DeathStatus])) {
      return EffectResult()
    }

    val status = DeathStatus(PassiveStatusId(s.nextEntityId), target)

    val experience = GainExperienceRule(s, this) map (x => {
      val (creature, experience) = x

      GainExperienceEffect(creature, experience, Some(this))
    })

    EffectResult(List(
      NewEntityMutation(status),
      RemovableEntityMutation(target)
    ), List(
      experience
    ).flatten)
  }
}
