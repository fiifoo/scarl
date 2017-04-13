package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.character.Progression.Step
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{CreatureLevelMutation, CreatureStatsMutation}

case class GainLevelEffect(target: CreatureId,
                           step: Step,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)

    EffectResult(List(
      CreatureLevelMutation(creature.id, creature.level + 1),
      CreatureStatsMutation(creature.id, creature.stats.add(step.stats))
    ))
  }
}
