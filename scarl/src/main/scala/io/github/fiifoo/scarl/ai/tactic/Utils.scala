package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{MoveAction, PassAction}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

object Utils {

  def roam(s: State, actor: CreatureId, tactic: Tactic, random: Random): Result = {
    if (getCreatureStats(s)(actor).speed <= 0) {
      (tactic, PassAction)
    } else {
      val from = actor(s).location
      val to = randomLocation(from, random)
      val action = MoveAction(to)

      (tactic, action)
    }
  }

  private def randomLocation(from: Location, random: Random): Location = {
    val to = Location(from.x + random.nextInt(3) - 1, from.y + random.nextInt(3) - 1)

    if (to == from) {
      randomLocation(from, random)
    } else {
      to
    }
  }
}
