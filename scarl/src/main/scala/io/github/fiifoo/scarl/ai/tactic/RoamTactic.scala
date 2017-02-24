package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.ai.SeekEnemy
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, Rng, State}

import scala.util.Random

case class RoamTactic(actor: CreatureId) extends Tactic {
  def apply(s: State, rng: Rng): (Tactic, Action, Rng) = {
    val enemy = SeekEnemy(s, actor(s))

    enemy map (enemy => {

      val charge = ChargeTactic(actor, SafeCreatureId(enemy.id), enemy.location)
      charge(s, rng)

    }) getOrElse {

      val (random, nextRng) = rng()
      val from = actor(s).location
      val to = randomLocation(from, random)
      val move = MoveAction(to)

      (this, move, nextRng)
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
