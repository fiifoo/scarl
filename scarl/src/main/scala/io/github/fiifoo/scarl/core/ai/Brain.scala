package io.github.fiifoo.scarl.core.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain.Intentions
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

case object Brain {
  type Intentions = Map[CreatureId, List[(Intention, Priority.Value)]]
}

case class Brain(faction: FactionId,
                 strategy: Strategy,
                 intentions: Intentions = Map(),
                ) {

  def apply(s: State, random: Random) = strategy(s, this, random)
}
