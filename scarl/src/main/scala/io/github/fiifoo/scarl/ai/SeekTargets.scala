package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Distance

object SeekTargets {

  val distance = Distance.chebyshev _

  def apply(s: State, creature: CreatureId, factions: Set[FactionId], range: Int): List[Creature] = {
    val location = creature(s).location

    val filter = (target: Creature) =>
      target.id != creature && inRange(location, target, range)

    val targets = factions.foldLeft(Set[Creature]())((targets, faction) => {
      val filtered = s.index.factionMembers getOrElse(faction, Set()) map (_ (s)) filter filter

      targets ++ filtered
    })

    targets
      .toList
      .sortWith((a, b) => distance(location, a.location) < distance(location, b.location))
  }

  private def inRange(from: Location, target: Creature, range: Int): Boolean = {
    distance(from, target.location) <= range
  }
}
