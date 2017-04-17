package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{Creature, FactionId}
import io.github.fiifoo.scarl.geometry.Distance

object SeekTargets {

  def apply(s: State, creature: Creature, factions: Set[FactionId], range: Int): List[Creature] = {
    val filter = (target: Creature) =>
      target != creature && inRange(creature, target, range)

    val targets = factions.foldLeft(Set[Creature]())((targets, faction) => {
      val filtered = s.index.factionMembers getOrElse(faction, Set()) map (_ (s)) filter filter

      targets ++ filtered
    })

    targets
      .toList
      .sortWith((a, b) => distance(creature, a) < distance(creature, b))
  }

  private def inRange(creature: Creature, target: Creature, range: Int): Boolean = {
    distance(creature, target) <= range
  }

  private def distance(creature: Creature, target: Creature): Int = {
    val from = creature.location
    val to = target.location

    Distance.chebyshev(from, to)
  }
}
