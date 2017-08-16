package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.ai.{Greeting, SeekEnemy}
import io.github.fiifoo.scarl.core.action.Behavior
import io.github.fiifoo.scarl.core.action.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

case object RoamTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    val enemy = SeekEnemy(s, actor)

    enemy flatMap (enemy => {
      charge(s, actor, enemy, random)
    }) getOrElse {
      roam(s, actor, random)
    }
  }

  private def roam(s: State, actor: CreatureId, random: Random): Result = {
    val action = Greeting(s, actor) getOrElse {
      val from = actor(s).location
      val to = randomLocation(from, random)

      MoveAction(to)
    }

    (this, action)
  }

  private def charge(s: State, actor: CreatureId, enemy: Creature, random: Random): Option[Result] = {
    val tactic = ChargeTactic(SafeCreatureId(enemy.id), enemy.location)

    tactic(s, actor, random)
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
