package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{AttackAction, MoveAction, PassAction, ShootAction}
import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry._

import scala.util.Random

case class ChargeTactic(actor: CreatureId,
                        target: SafeCreatureId,
                        destination: Location
                       ) extends Tactic {
  type Result = (Tactic, Action)

  def apply(s: State, random: Random): Result = {
    target(s) map (target => {
      chargeOrPursue(s, random, target)
    }) getOrElse {
      roam(s, random)
    }
  }

  private def chargeOrPursue(s: State, random: Random, target: Creature): Result = {
    val line = Line(actor(s).location, target.location)
    val range = actor(s).stats.sight.range

    if (line.size <= range + 1 && Los(s)(line)) {
      charge(s, target, line.size <= 2, line)
    } else {
      pursue(s, random)
    }
  }

  private def charge(s: State, target: Creature, adjacent: Boolean, line: Vector[Location]): Result = {
    val tactic = copy(destination = target.location)
    val action = if (adjacent) {
      AttackAction(target.id)
    } else if (couldShoot(s, line)) {
      ShootAction(target.location)
    } else {
      Path(s)(actor(s).location, target.location) map (path => {
        MoveAction(path.head)
      }) getOrElse {
        PassAction()
      }
    }

    (tactic, action)
  }

  private def pursue(s: State, random: Random): Result = {
    PursueTactic(actor, target, destination)(s, random)
  }

  private def roam(s: State, random: Random): Result = {
    RoamTactic(actor)(s, random)
  }

  private def couldShoot(s: State, line: Vector[Location]): Boolean = {
    val range = getCreatureStats(s)(actor).ranged.range
    val distance = line.length - 1

    range >= distance && !line.tail.init.exists(Obstacle.shot(s)(_).isDefined)
  }
}
