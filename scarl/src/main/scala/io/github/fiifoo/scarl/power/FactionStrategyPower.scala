package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Strategy
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.area.FactionStrategyEffect

case class FactionStrategyPower(description: Option[String] = None,
                                resources: Option[Resources] = None,
                                strategy: Option[Strategy],
                               ) extends CreaturePower {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    List(
      FactionStrategyEffect(creature(s).faction, this.strategy),
    )
  }
}
