package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.CreatureExperienceMutation

case class GainExperienceEffect(target: CreatureId,
                                amount: Int,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val experience = target(s).experience + amount

    EffectResult(CreatureExperienceMutation(target, experience))
  }
}
