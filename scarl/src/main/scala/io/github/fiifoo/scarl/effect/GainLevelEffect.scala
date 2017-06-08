package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Progression.Step
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{CreatureCharacterMutation, CreatureStatsMutation}

case class GainLevelEffect(target: CreatureId,
                           step: Step,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    target(s).character map (character => {
      EffectResult(List(
        CreatureCharacterMutation(target, character.copy(level = character.level + 1)),
        CreatureStatsMutation(target, target(s).stats.add(step.stats))
      ))
    }) getOrElse EffectResult()
  }
}
