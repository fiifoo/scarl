package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectListener}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.effect.DeathEffect

class StatisticsBuilder(initial: Statistics = Statistics()) extends EffectListener {

  private var deaths: Map[CreatureKindId, Int] = initial.deaths

  def apply(s: State, effect: Effect): Unit = {
    effect match {
      case effect: DeathEffect => death(s, effect.target)
      case _ =>
    }
  }

  def get(): Statistics = Statistics(deaths)

  private def death(s: State, creature: CreatureId): Unit = {
    val kind = creature(s).kind
    deaths = deaths + (kind -> (deaths.getOrElse(kind, 0) + 1))
  }
}
