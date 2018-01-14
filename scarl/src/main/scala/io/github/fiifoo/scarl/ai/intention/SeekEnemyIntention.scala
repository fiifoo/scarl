package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.ChargeTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureComrades
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.{Line, Los}

import scala.util.Random

object SeekEnemyIntention extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    val enemy = getVisibleEnemy(s, actor) orElse getPartyEnemy(s, actor)

    enemy flatMap charge(s, actor, random)
  }

  private def charge(s: State, actor: CreatureId, random: Random)(enemy: Creature): Option[Result] = {
    val tactic = ChargeTactic(SafeCreatureId(enemy.id), enemy.location)

    tactic(s, actor, random)
  }

  private def getVisibleEnemy(s: State, actor: CreatureId): Option[Creature] = {
    val creature = actor(s)
    val los = Los(s) _

    getEnemies(s, creature) find (candidate => {
      val line = Line(creature.location, candidate.location)

      los(line)
    })
  }

  private def getPartyEnemy(s: State, creature: CreatureId): Option[Creature] = {
    getCreatureComrades(s)(creature) flatMap s.tactics.get collectFirst {
      case tactic: ChargeTactic if tactic.target(s).isDefined => tactic.target(s).get
    }
  }

  private def getEnemies(s: State, creature: Creature): List[Creature] = {
    val factions = creature.faction(s).enemies
    val range = creature.stats.sight.range

    Utils.findTargets(s, creature.id, factions, range)
  }
}
