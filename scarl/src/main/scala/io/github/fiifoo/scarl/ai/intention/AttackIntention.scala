package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.ai.tactic.AttackTactic
import io.github.fiifoo.scarl.core.State
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
    val action = if (isAdjacent(line)) {
      Some(AttackAction(target.id))
    } else if (shouldShootMissile(s, actor, line)) {
      Some(ShootMissileAction(target.location))
    } else if (shouldShoot(s, actor, line)) {
      Some(ShootAction(target.location))
    } else {
      None
    }

    action map (action => {
      val tactic = AttackTactic(SafeCreatureId(target.id), target.location, enableMove)

      (tactic, action)
    })
  }

  private def shouldShootMissile(s: State, actor: CreatureId, line: Vector[Location]): Boolean = {
    val stats = getCreatureStats(s)(actor)

    if (s.simulation.running || stats.launcher.missile.isEmpty || !couldShoot(s, line, stats.launcher.range)) {
      false
    } else {
      ShootMissileOutcomeSimulation(s, actor, line.last) == Outcome.Good
    }
  }

  private def shouldShoot(s: State, actor: CreatureId, line: Vector[Location]): Boolean = {
    couldShoot(s, line, getCreatureStats(s)(actor).ranged.range)
  }

  private def couldShoot(s: State, line: Vector[Location], range: Int): Boolean = {
    val distance = line.length - 1

    range >= distance && !line.tail.init.exists(Obstacle.has(Obstacle.shot(s)))
  }

  private def isAdjacent(line: Vector[Location]): Boolean = line.size <= 2
}
