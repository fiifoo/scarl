package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{LocatableId, Signal}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.effect.area.SignalEffect
import io.github.fiifoo.scarl.rule.ExplosionRule

case class ExplosionEffect(source: LocatableId,
                           location: Location,
                           stats: Explosive,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val locations = ExplosionRule(s, location, stats)
    val signal = SignalEffect(Signal.NoiseSignal, location, Signal.Strong)

    EffectResult(
      signal :: (locations map (ExplosionLocationEffect(source, _, stats, Some(this)))).toList
    )
  }

}
