package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats.Explosive
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class ExplodeEffect(explosive: LocatableId,
                         location: Location,
                         stats: Explosive,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(
      RemovableEntityMutation(explosive),
      ExplosionEffect(explosive, location, stats, Some(this)),
    )
  }
}
