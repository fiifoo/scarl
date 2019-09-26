package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.{Stance, Stats}
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId

object Stances {

  case object Aim extends Stance {
    val key = "aim"
    val duration = 2

    def modifyStats(stats: Stats): Stats = {
      stats.copy(
        ranged = stats.ranged.copy(
          attack = (stats.ranged.attack * 1.2).toInt,
        ),
        speed = stats.speed / 2,
        defence = (stats.defence * 0.8).toInt,
      )
    }

    def effects(s: State, creature: CreatureId): List[Effect] = List()
  }

}
