package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Character
import io.github.fiifoo.scarl.core.creature.Progression.Step
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.CreatureCharacterMutation

case class GainExperienceEffect(target: CreatureId,
                                amount: Int,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    target(s).character map (character => {
      val steps = progressionSteps(s, character)

      EffectResult(
        CreatureCharacterMutation(target, character.copy(experience = character.experience + amount)),
        steps map (GainLevelEffect(target, _))
      )
    }) getOrElse EffectResult()
  }

  private def progressionSteps(s: State, character: Character): List[Step] = {
    val progression = s.progressions(character.progression)

    val filter = (step: Step) => {
      step.requirements.experience > character.experience &&
        step.requirements.experience <= character.experience + amount
    }

    progression.steps.filter(filter).toList
  }
}
