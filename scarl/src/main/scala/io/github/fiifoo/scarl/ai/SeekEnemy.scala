package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.ai.tactic.ChargeTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureComrades
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.geometry.{Line, Los}

object SeekEnemy {

  def apply(s: State, creature: CreatureId): Option[Creature] = {
    visible(s, creature) orElse party(s, creature)
  }

  def visible(s: State, creature: CreatureId): Option[Creature] = {
    val los = Los(s) _

    for (candidate <- getCandidates(s, creature(s))) {
      val line = Line(creature(s).location, candidate.location)
      if (los(line)) {
        return Some(candidate)
      }
    }

    None
  }

  def party(s: State, creature: CreatureId): Option[Creature] = {
    getCreatureComrades(s)(creature) flatMap s.tactics.get collectFirst {
      case tactic: ChargeTactic if tactic.target(s).isDefined => tactic.target(s).get
    }
  }

  private def getCandidates(s: State, creature: Creature): List[Creature] = {
    val factions = creature.faction(s).enemies
    val range = creature.stats.sight.range

    SeekTargets(s, creature.id, factions, range)
  }
}
