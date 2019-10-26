package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.{Stance, Stats}
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId

object Stances {

  case object Aim extends Stance {
    val key = "Stances.Aim"
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

  case object Evasive extends Stance {
    val key = "Stances.Evasive"
    val duration = 1

    def modifyStats(stats: Stats): Stats = {
      stats.copy(
        melee = stats.melee.copy(
          attack = (stats.melee.attack * 0.8).toInt,
        ),
        ranged = stats.ranged.copy(
          attack = (stats.ranged.attack * 0.8).toInt,
        ),
        defence = (stats.defence * 1.2).toInt,
      )
    }

    def effects(s: State, creature: CreatureId): List[Effect] = List()
  }

  case object Vulnerable extends Stance {
    val key = "Stances.Vulnerable"
    val duration = 1

    def modifyStats(stats: Stats): Stats = {
      stats.copy(
        armor = (stats.armor * 0.5).toInt,
      )
    }

    def effects(s: State, creature: CreatureId): List[Effect] = List()
  }
}
