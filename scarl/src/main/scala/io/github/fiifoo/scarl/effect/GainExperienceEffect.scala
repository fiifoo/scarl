package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.character.Progression.Step
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation.CreatureExperienceMutation

case class GainExperienceEffect(target: CreatureId,
                                amount: Int,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val steps = progressionSteps(s, creature)

    EffectResult(
      CreatureExperienceMutation(creature.id, creature.experience + amount),
      steps map (GainLevelEffect(creature.id, _))
    )
  }

  private def progressionSteps(s: State, creature: Creature): List[Step] = {
    val progression = creature.progression flatMap s.progressions.get

    val filter = (step: Step) => {
      step.requirements.experience > creature.experience &&
        step.requirements.experience <= creature.experience + amount
    }

    progression map (_.steps.filter(filter).toList) getOrElse List()
  }
}
