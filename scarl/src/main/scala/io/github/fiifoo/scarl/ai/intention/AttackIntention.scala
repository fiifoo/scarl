package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.ai.tactic.AttackTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry._
import io.github.fiifoo.scarl.simulation.{Outcome, ShootMissileOutcomeSimulation}

import scala.util.Random

case class AttackIntention(target: SafeCreatureId, enableMove: Boolean = true) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    target(s) flatMap (target => {
      val line = Line(actor(s).location, target.location)
      val range = actor(s).stats.sight.range

      if (line.size <= range + 1 && Los(s)(line)) {
        attack(s, actor, target, line)
      } else {
        None
      }
    })
  }

  private def attack(s: State, actor: CreatureId, target: Creature, line: Vector[Location]): Option[Result] = {
    val action =
      attackAction(s, actor, target, line) orElse
        shootMissileAction(s, actor, target, line) orElse
        shootAction(s, actor, target, line)

    action map (action => {
      val tactic = AttackTactic(SafeCreatureId(target.id), target.location, enableMove)

      (tactic, action)
    })
  }

  private def attackAction(s: State, actor: CreatureId, target: Creature, line: Vector[Location]): Option[Action] = {
    if (isAdjacent(line)) {
      Some(AttackAction(target.id))
    } else {
      None
    }
  }

  private def shootMissileAction(s: State, actor: CreatureId, target: Creature, line: Vector[Location]): Option[Action] = {
    val stats = getCreatureStats(s)(actor)

    if (s.simulation.running || stats.launcher.missiles.isEmpty || !couldShoot(s, line, stats.launcher.range)) {
      None
    } else {
      val missile = stats.launcher.missiles.head

      if (ShootMissileOutcomeSimulation(s, actor, line.last, missile) == Outcome.Good) {
        Some(ShootMissileAction(target.location, missile))
      } else {
        None
      }
    }
  }

  private def shootAction(s: State, actor: CreatureId, target: Creature, line: Vector[Location]): Option[Action] = {
    if (couldShoot(s, line, getCreatureStats(s)(actor).ranged.range)) {
      Some(ShootAction(target.location))
    } else {
      None
    }
  }

  private def couldShoot(s: State, line: Vector[Location], range: Int): Boolean = {
    val distance = line.length - 1

    range >= distance && !line.tail.init.exists(Obstacle.has(Obstacle.shot(s)))
  }

  private def isAdjacent(line: Vector[Location]): Boolean = line.size <= 2
}
