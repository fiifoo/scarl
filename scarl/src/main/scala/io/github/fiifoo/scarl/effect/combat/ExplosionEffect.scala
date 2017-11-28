package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.rule.ExplosionRule

case class ExplosionEffect(source: LocatableId,
                           location: Location,
                           stats: Explosive,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val locations = ExplosionRule(s, location, stats)

    EffectResult(
      (locations map (ExplosionLocationEffect(source, _, stats, Some(this)))).toList
    )
  }

}
