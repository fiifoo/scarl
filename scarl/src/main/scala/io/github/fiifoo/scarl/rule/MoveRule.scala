package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Selectors, State, Time}

object MoveRule {

  def cost(s: State, creature: CreatureId): Int = {
    val speed = Selectors.getCreatureStats(s)(creature).speed

    (Time.turn / speed).round.toInt
  }

}
