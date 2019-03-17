package io.github.fiifoo.scarl.effect.creature.condition

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ConditionStatus, CreatureId, Selectors}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.{ConditionStrengthMutation, RngMutation}
import io.github.fiifoo.scarl.rule.ConditionRule

case class ApplyConditionEffect(source: ConditionStatus,
                                target: CreatureId,
                                duration: Tick,
                                location: Location,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng.apply()
    val condition = this.source.condition
    val resistance = condition.resistance(Selectors.getCreatureStats(s)(this.target))

    val effects = condition.effects(s, this.target, this.source.strength)
    val mutations = List(RngMutation(rng))

    if (duration == 0) {
      EffectResult(
        mutations,
        effects
      )
    } else {
      ConditionRule(random)(this.source.strength, resistance) map (strength => {
        if (strength != this.source.strength) {
          EffectResult(
            mutations ::: List(ConditionStrengthMutation(this.source, strength)),
            effects
          )
        } else {
          EffectResult(
            mutations,
            effects
          )
        }
      }) getOrElse {
        EffectResult(
          mutations,
          effects ::: List(LoseConditionEffect(this.source, this.target, this.location, Some(this)))
        )
      }
    }
  }
}
