package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.geometry.{Line, Los}

object SeekEnemy {

  private def _range = 5 // should come from creature

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
    val candidates: Iterable[Creature] = s.entities.values collect {
      case candidate: Creature if candidate != creature && inRange(creature, candidate, _range) => candidate
    }

    candidates.toList sortWith ((a, b) => distance(creature, a) < distance(creature, b))
  }

  private def inRange(creature: Creature, candidate: Creature, range: Int): Boolean = {
    distance(creature, candidate) <= range
  }

  private def distance(creature: Creature, candidate: Creature): Int = {
    val a = creature.location
    val b = candidate.location

    Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y))
  }
}
