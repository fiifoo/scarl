package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.{Condition, Stats}
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.creature.condition.BurnEffect

object Conditions {

  case class Burning(strength: Int) extends Condition {
    val key = "Conditions.Burning"

    def modifyStats(stats: Stats, strength: Int): Stats = {
      stats.copy(
        speed = (stats.speed * 0.7).toInt,
      )
    }

    def resistance(stats: Stats): Int = {
      (stats.resistance + stats.armor) / 2
    }

    def effects(s: State, creature: CreatureId, strength: Int): List[Effect] = {
      List(
        BurnEffect(creature, strength, creature(s).location)
      )
    }
  }

  case class Disoriented(strength: Int) extends Condition {
    val key = "Conditions.Disoriented"

    def modifyStats(stats: Stats, strength: Int): Stats = {
      stats.copy(
        speed = stats.speed / 2,
        defence = stats.defence / 2,
        melee = stats.melee.copy(
          attack = stats.melee.attack / 2
        ),
        ranged = stats.ranged.copy(
          attack = stats.ranged.attack / 2
        )
      )
    }

    def resistance(stats: Stats): Int = {
      (stats.resistance + stats.sight.sensors) / 2
    }

    def effects(s: State, creature: CreatureId, strength: Int): List[Effect] = {
      List()
    }
  }

  case class Immobilized(strength: Int) extends Condition {
    val key = "Conditions.Immobilized"

    def modifyStats(stats: Stats, strength: Int): Stats = {
      stats.copy(
        speed = 0,
        defence = stats.defence / 2
      )
    }

    def resistance(stats: Stats): Int = {
      stats.resistance
    }

    def effects(s: State, creature: CreatureId, strength: Int): List[Effect] = {
      List()
    }
  }

}
