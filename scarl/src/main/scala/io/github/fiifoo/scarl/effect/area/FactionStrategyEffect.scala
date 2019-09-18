package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Strategy
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.mutation.FactionStrategyMutation

case class FactionStrategyEffect(faction: FactionId,
                                 strategy: Option[Strategy],
                                 parent: Option[Effect] = None
                                ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(FactionStrategyMutation(this.faction, this.strategy))
  }
}
