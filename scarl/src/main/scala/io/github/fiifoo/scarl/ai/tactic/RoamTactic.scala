package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.ai.SeekEnemy
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

case class RoamTactic(actor: CreatureId) extends Tactic {
  type Result = (Tactic, Action)

  def apply(s: State, random: Random): Result = {
    val enemy = SeekEnemy(s, actor(s))

    enemy map (enemy => {
      charge(s, random, enemy)
    }) getOrElse {
      roam(s, random)
    }
  }

  private def roam(s: State, random: Random): Result = {
    val from = actor(s).location
    val to = randomLocation(from, random)
    val action = MoveAction(to)

    (this, action)
  }

  private def charge(s: State, random: Random, enemy: Creature): Result = {
    val tactic = ChargeTactic(actor, SafeCreatureId(enemy.id), enemy.location)

    tactic(s, random)
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
