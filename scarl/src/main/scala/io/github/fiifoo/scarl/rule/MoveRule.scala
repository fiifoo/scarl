package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.entity.{CreatureId, Selectors}
import io.github.fiifoo.scarl.core.{State, Time}

object MoveRule {
  val baseSpeed = 100
  val minSpeed = 10

  def cost(s: State, creature: CreatureId): Int = {
    val speed = math.max(minSpeed, Selectors.getCreatureStats(s)(creature).speed)

    (Time.turn.toDouble * baseSpeed / speed).round.toInt
  }

}
