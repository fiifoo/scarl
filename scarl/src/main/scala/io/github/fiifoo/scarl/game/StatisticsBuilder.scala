package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.effect.combat.DeathEffect

object StatisticsBuilder {

  def apply(s: State, current: Statistics, effects: List[Effect]): Statistics = {
    val deaths = effects collect {
      case effect: DeathEffect => effect
    }

    deaths.foldLeft(current)(death(s))
  }

  private def death(s: State)(current: Statistics, effect: DeathEffect): Statistics = {
    val kind = effect.target(s).kind

    current.copy(
      deaths = current.deaths + (kind -> (current.deaths.getOrElse(kind, 0) + 1))
    )
  }
}
