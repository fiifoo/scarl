package io.github.fiifoo.scarl.effect.creature.condition

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Selectors}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.ConditionRule

case class RollConditionsEffect(target: CreatureId,
                                conditions: List[Condition],
                                location: Location,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    if (this.target(s).dead) {
      return EffectResult()
    }

    val (random, rng) = s.rng.apply()
    val stats = Selectors.getCreatureStats(s)(this.target)

    val effects = this.conditions map (condition => {
      val resistance = condition.resistance(stats)

      ConditionRule(random)(condition.strength, resistance) map (strength => {
        GainConditionEffect(this.target, condition, strength, location, Some(this))
      }) getOrElse {
        ResistConditionEffect(this.target, condition, location, Some(this))
      }
    })

    EffectResult(
      RngMutation(rng),
      effects
    )
  }
}
