package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.geometry.{Line, Los}

object SeekEnemy {

  def apply(s: State, creature: Creature): Option[Creature] = {
    val los = Los(s) _

    for (candidate <- getCandidates(s, creature)) {
      val line = Line(creature.location, candidate.location)
      if (los(line)) {
        return Some(candidate)
      }
    }

    None
  }

  private def getCandidates(s: State, creature: Creature): List[Creature] = {
    val factions = creature.faction(s).enemies
    val range = creature.stats.sight.range

    SeekTargets(s, creature, factions, range)
  }
}
