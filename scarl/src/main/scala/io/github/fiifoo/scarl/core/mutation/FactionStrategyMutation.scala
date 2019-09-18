package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.{Brain, Strategy}
import io.github.fiifoo.scarl.core.creature.FactionId

case class FactionStrategyMutation(faction: FactionId, strategy: Option[Strategy]) extends Mutation {
  def apply(s: State): State = {
    strategy orElse s.assets.factions(faction).strategy map (strategy => {
      val brain = Brain(faction, strategy)

      s.copy(
        brains = s.brains + (faction -> brain)
      )
    }) getOrElse {
      s.copy(
        brains = s.brains - faction
      )
    }
  }
}
