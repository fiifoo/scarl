package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{CreatureDeadMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.rule.GainExperienceRule

case class DeathEffect(target: CreatureId,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    if (target(s).dead) {
      return EffectResult()
    }

    val experience = GainExperienceRule(s, this) map (x => {
      val (creature, experience) = x

      GainExperienceEffect(creature, experience, Some(this))
    })

    EffectResult(List(
      CreatureDeadMutation(target),
      RemovableEntityMutation(target)
    ), List(
      experience
    ).flatten)
  }
}
