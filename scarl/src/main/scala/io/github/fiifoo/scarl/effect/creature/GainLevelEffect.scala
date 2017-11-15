package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.creature.Progression.Step
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{CreatureCharacterMutation, CreatureStatsMutation}
import io.github.fiifoo.scarl.core.{Location, State}

case class GainLevelEffect(target: CreatureId,
                           step: Step,
                           location: Location,
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
